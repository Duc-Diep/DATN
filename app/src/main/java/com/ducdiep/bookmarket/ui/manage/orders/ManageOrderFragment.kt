package com.ducdiep.bookmarket.ui.manage.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_MANAGE_BOOK
import com.ducdiep.bookmarket.base.KEY_MANAGE_ORDER
import com.ducdiep.bookmarket.databinding.FragmentManageOrderBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.manage.ManageActivity
import com.ducdiep.bookmarket.ui.manage.books.AddOrEditBookFragment

class ManageOrderFragment : BaseFragment(R.layout.fragment_manage_order) {
    private val binding by viewBinding(FragmentManageOrderBinding::bind)
    lateinit var manageOrderViewModel: ManageOrderViewModel
    lateinit var manageOrderAdapter: ManageOrderAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserve()
    }

    private fun initViews() {
        context?.let {
            manageOrderAdapter = ManageOrderAdapter(it, listOf(), {
                (context as ManageActivity).setCurrentFragment(
                    ManageOrderDetailFragment.newInstance(
                        it.order_id,
                        it.user_id
                    )
                )
                (context as ManageActivity).manageViewModel.stackFragment.add(KEY_MANAGE_ORDER)
            }, { orderId, status ->
                manageOrderViewModel.updateStatus(orderId,status)
            })
            binding.rcvManageOrder.apply {
                adapter = manageOrderAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(it, RecyclerView.VERTICAL))
            }

        }
    }

    private fun initObserve() {
        manageOrderViewModel = ViewModelProvider(this).get(ManageOrderViewModel::class.java)
        manageOrderViewModel.listOrder.observe(viewLifecycleOwner) {
            manageOrderAdapter.setData(it)
        }
    }
}