package com.ducdiep.bookmarket.ui.client.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ScrollView
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.FragmentHomeBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.ui.client.home.adapter.BookAdapter
import com.ducdiep.bookmarket.ui.client.home.adapter.SlideAdapter
import com.ducdiep.bookmarket.ui.client.home.adapter.SlideModel
import com.ducdiep.bookmarket.ui.client.home.bookdetails.BookDetailFragment
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.ui.client.search.SearchFragment

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var homeViewModel: HomeViewModel
    private val binding by viewBinding(FragmentHomeBinding::bind)
    var listSlideModel: ArrayList<SlideModel> = arrayListOf()
    var handler = Handler(Looper.getMainLooper())
    var runnable = Runnable {
        var currentPosition = binding.vpSlide.currentItem
        if (currentPosition == 3) {
            binding.vpSlide.setCurrentItem(0, true)
        } else {
            binding.vpSlide.setCurrentItem(currentPosition + 1, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.tvNewBook.setOnClickListener {
            navigateToSearch("Sách mới cập nhật","")
        }
        binding.tvNewComic.setOnClickListener {
            navigateToSearch("Truyện tranh", ID_COMIC)
        }
        binding.tvNewEconomic.setOnClickListener {
            navigateToSearch("Sách kinh tế", ID_ECONOMIC)
        }
        binding.tvNewLiterature.setOnClickListener {
            navigateToSearch("Sách văn học", ID_LITERATURE)
        }
    }

    private fun navigateToSearch(title: String, id: String) {
        (context as MainActivity).setCurrentFragment(SearchFragment.newInstance(title, id))
        (context as MainActivity).mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
    }

    private fun initObserve() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.listBooks.observe(viewLifecycleOwner) {
            filterSlide(it)
            setupRcvAdapter(it)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.rlLoading.visibility = View.VISIBLE
                binding.root.children.forEach {
                    if (it is ScrollView) {
                        it.visibility = View.GONE
                    }
                }
            } else {
                binding.rlLoading.visibility = View.GONE
                binding.root.children.forEach {
                    if (it is ScrollView) {
                        it.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRcvAdapter(list: ArrayList<Book>) {
        if (list.isNotEmpty()) {
            context?.let {
                // adapter new book
                val bookAdapter = BookAdapter(it, list) {
                    navigateToDetail(it)
                }
                binding.rcvNewBook.apply {
                    adapter = bookAdapter
                    setHasFixedSize(true)
                }
                // adapter truyen tranh
                val listComic = list.filter {
                    it.category_id == ID_COMIC
                }
                val comicAdapter = BookAdapter(it, listComic) {
                    navigateToDetail(it)
                }
                binding.rcvComicBook.apply {
                    adapter = comicAdapter
                }

                // adapter economic
                val listEconomic = list.filter {
                    it.category_id == ID_ECONOMIC
                }
                val economicAdapter = BookAdapter(it, listEconomic) {
                    navigateToDetail(it)
                }
                binding.rcvEconomicBook.apply {
                    adapter = economicAdapter
                }

                // adapter literature
                val listLiterature = list.filter {
                    it.category_id == ID_LITERATURE
                }
                val literatureAdapter = BookAdapter(it, listLiterature) {
                    navigateToDetail(it)
                }
                binding.rcvLiteratureBook.apply {
                    adapter = literatureAdapter
                }
            }
        }
    }

    private fun navigateToDetail(book: Book) {
        (context as MainActivity).setCurrentFragment(BookDetailFragment.newInstance(book))
        (context as MainActivity).mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
    }

    private fun filterSlide(arrayList: ArrayList<Book>) {
        if (arrayList.isNotEmpty()) {
            listSlideModel.clear()
            arrayList.find {
                it.name == "Quản Lý Những Điều Cốt Lõi"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide1, it))
            }
            arrayList.find {
                it.name == "Giải Mã Chiến Lược Đông Tây"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide2, it))
            }
            arrayList.find {
                it.name == "Ép Mình Phải Xinh Phải Dữ"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide3, it))
            }
            arrayList.find {
                it.name == "Ra Bờ Suối Ngắm Hoa Kèn Hồng"
            }?.let {
                listSlideModel.add(SlideModel(R.drawable.slide4, it))
            }
            setupSideAdapter()
        }
    }

    private fun setupSideAdapter() {
        view?.let {
            val slideAdapter = SlideAdapter(requireContext(), listSlideModel) {
                navigateToDetail(it)
            }
            binding.vpSlide.adapter = slideAdapter
            binding.ciDot.setViewPager(binding.vpSlide)
            binding.vpSlide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 3000)
                }
            })
        }
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    companion object {
        fun newInstance() =
            HomeFragment().apply {

            }
    }
}