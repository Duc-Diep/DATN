package com.ducdiep.bookmarket.ui.client.home.bookdetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.FragmentBookDetailsBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.extensions.getTimeNow
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.ProductRating
import com.ducdiep.bookmarket.sqlite.SQLHelper
import com.ducdiep.bookmarket.ui.client.home.adapter.BookAdapter
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class BookDetailFragment : BaseFragment(R.layout.fragment_book_details) {
    private lateinit var bookDetailViewModel: BookDetailViewModel
    private val binding by viewBinding(FragmentBookDetailsBinding::bind)
    var book: Book? = null
    lateinit var sqlHelper: SQLHelper
    lateinit var bookAdapter: BookAdapter
    lateinit var commentAdapter: CommentAdapter
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            book = it.getSerializable("book") as? Book
        }
        initViews()
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.imgBack.setOnClickListener {
            (context as MainActivity).onBackPressed()
        }
        binding.btnAddToCart.setOnClickListener {
            book?.book_id?.let { book ->
                sqlHelper.insertOrUpdateBookToCart(book)
                Toast.makeText(
                    requireContext(),
                    "Thêm sản phẩm vào giỏ hàng thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnComment.setOnClickListener {
            if (binding.edtComment.text.toString().isNotEmpty()) {
                book?.let { book ->
                    uid?.let { id ->
                        bookDetailViewModel.addComment(
                            ProductRating(
                                book.book_id, binding.edtComment.text.toString(),
                                getTimeNow(), "", binding.rbStarComment.rating, getTimeNow(), id
                            )
                        )
                        binding.edtComment.setText("")
                        binding.rbStarComment.rating = 0F
                    }
                }
            }
        }
        binding.tvMore.setOnClickListener {
            if (binding.tvMore.text == getString(R.string.more)) {
                binding.tvDescription.maxLines = 999
                binding.tvMore.text = getString(R.string.unmore)
            } else {
                binding.tvDescription.maxLines = 4
                binding.tvMore.text = getString(R.string.more)
            }
        }
    }

    private fun initObserve() {
        bookDetailViewModel = ViewModelProvider(this).get(BookDetailViewModel::class.java)
        book?.let { bookDetailViewModel.getAllBooksByCategoryId(it.category_id, it.book_id) }
        bookDetailViewModel.listRecommendBooks.observe(viewLifecycleOwner) {
            bookAdapter.setData(it)
        }
        bookDetailViewModel.getListRating(book?.book_id!!)

        bookDetailViewModel.listRating.observe(viewLifecycleOwner) {
            calculateStar(it)
            bookDetailViewModel.getListComment()
        }
        bookDetailViewModel.listComment.observe(viewLifecycleOwner) {
            commentAdapter.setData(it)
        }
    }

    private fun calculateStar(list: ArrayList<ProductRating>?) {
        view?.let {
            var sumStar = 0F
            var sumRate = 0
            list?.forEach {
                sumRate += 1
                sumStar += it.star
            }
            binding.rbStar.rating = sumStar / sumRate
        }
    }

    private fun initViews() {
        view?.let {
            context?.let {
                sqlHelper = SQLHelper(it)
                bookAdapter = BookAdapter(it, listOf()) {
                    (context as MainActivity).setCurrentFragment(newInstance(it))
                    (context as MainActivity).mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
                }
                binding.rcvRecommendBook.apply {
                    adapter = bookAdapter
                    setHasFixedSize(true)
                }

                commentAdapter = CommentAdapter(it, listOf())
                binding.rcvComment.apply {
                    adapter = commentAdapter
                    setHasFixedSize(true)
                    addItemDecoration(DividerItemDecoration(it, RecyclerView.VERTICAL))
                }
            }

            book?.let { b ->
                binding.apply {
                    Glide.with(requireContext()).load(b.image).into(imgBookBigImage)
                    Glide.with(requireContext()).load(b.image).into(imgBookImage)
                    tvBookName.text = b.name
                    tvAuthor.text = b.author
                    rbStar.rating = 5F
                    tvPriceReal.text = "Giá gốc: ${getFormatMoney(b.price)} vnđ"
                    tvPriceDiscount.text =
                        "Giá bán: ${getFormatMoney(b.price * 85 / 100)} vnđ (Giảm giá: 15%)"
                    tvStatus.text =
                        if (b.current_number > 0) "Trạng thái: Còn hàng" else "Trạng thái: Hết hàng"
                    tvAuthorr.text = b.author
                    tvPublisher.text = b.publisher
                    tvDate.text = b.created_at.split(" ")[0]
                    tvNumOfPage.text = b.num_of_page.toString()
                    tvDescription.text = b.description
                }
            }
        }
    }

    companion object {
        fun newInstance(book: Book) = BookDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("book", book)
            }
        }
    }
}