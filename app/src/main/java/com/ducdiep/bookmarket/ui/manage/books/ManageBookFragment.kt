package com.ducdiep.bookmarket.ui.manage.books

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentManageBookBinding
import com.ducdiep.bookmarket.extensions.showDialogConfirm
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book

class ManageBookFragment : BaseFragment(R.layout.fragment_manage_book) {
    private val binding by viewBinding(FragmentManageBookBinding::bind)
    lateinit var manageBookViewModel: ManageBookViewModel
    lateinit var bookAdapter: ManageBookAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initObserve() {
        manageBookViewModel = ViewModelProvider(this).get(ManageBookViewModel::class.java)
        manageBookViewModel.listBooks.observe(viewLifecycleOwner) {
            if (it != null) {
                setupAdapter(it)
            }
        }
    }

    private fun setupAdapter(it: ArrayList<Book>) {
        bookAdapter = ManageBookAdapter(requireContext(), it, {
                context?.showDialogConfirm("Bạn có chắc chắn muốn xóa sản phẩm này không?","Đồng ý", "Hủy bỏ"){
                    manageBookViewModel.removeById(it)
                }
        }, {
            //to book details
        })
        binding.rcvManageBooks.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            addItemDecoration(DividerItemDecoration(requireContext(),RecyclerView.VERTICAL))
        }
    }
}