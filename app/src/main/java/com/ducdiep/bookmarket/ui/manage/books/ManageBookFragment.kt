package com.ducdiep.bookmarket.ui.manage.books

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_MANAGE_BOOK
import com.ducdiep.bookmarket.databinding.FragmentManageBookBinding
import com.ducdiep.bookmarket.extensions.showDialogConfirm
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.ui.manage.ManageActivity

class ManageBookFragment : BaseFragment(R.layout.fragment_manage_book) {
    private val binding by viewBinding(FragmentManageBookBinding::bind)
    lateinit var manageBookViewModel: ManageBookViewModel
    lateinit var bookAdapter: ManageBookAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.btnAddBook.setOnClickListener {
            (context as ManageActivity).setCurrentFragment(AddOrEditBookFragment.newInstance(null))
            (context as ManageActivity).manageViewModel.stackFragment.add(KEY_MANAGE_BOOK)
        }
    }

    private fun initObserve() {
        manageBookViewModel = ViewModelProvider(this).get(ManageBookViewModel::class.java)
        manageBookViewModel.listBooks.observe(viewLifecycleOwner) {
            if (it != null) {
                setupAdapter(it)
            }
        }
        manageBookViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

    }

    private fun setupAdapter(listData: ArrayList<Book>) {
        context?.let {
            bookAdapter = ManageBookAdapter(it, listData, {
                context?.showDialogConfirm(
                    "Bạn có chắc chắn muốn xóa sản phẩm này không?",
                    "Đồng ý",
                    "Hủy bỏ"
                ) {
                    manageBookViewModel.removeById(it)
                }
            }, {
                (context as ManageActivity).setCurrentFragment(AddOrEditBookFragment.newInstance(it))
                (context as ManageActivity).manageViewModel.stackFragment.add(KEY_MANAGE_BOOK)
            })
            binding.rcvManageBooks.apply {
                adapter = bookAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
        }
    }
}