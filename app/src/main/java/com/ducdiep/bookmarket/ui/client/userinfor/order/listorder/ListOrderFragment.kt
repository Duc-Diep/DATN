package com.ducdiep.bookmarket.ui.client.userinfor.order.listorder

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_USER_INFOR_FRAGMENT
import com.ducdiep.bookmarket.base.STATUS_WAITING
import com.ducdiep.bookmarket.databinding.FragmentListOrderBinding
import com.ducdiep.bookmarket.extensions.showAlertDialog
import com.ducdiep.bookmarket.extensions.showAlertDialogConfirm
import com.ducdiep.bookmarket.extensions.showDialogConfirm
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Order
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail.OrderDetailFragment

class ListOrderFragment : BaseFragment(R.layout.fragment_list_order) {
    private val binding by viewBinding(FragmentListOrderBinding::bind)
    lateinit var listOrderViewModel: ListOrderViewModel
    lateinit var listOrderAdapter: ListOrderAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserve()
    }

    private fun initViews() {
        context?.let {
            listOrderAdapter = ListOrderAdapter(it, listOf(), {
                navigateToDetail(it)
            }, { order ->
                context?.let {
                    if (order.status == STATUS_WAITING) {
                        it.showDialogConfirm(
                            "Bạn có chắc chắn muốn hủy đơn hàng này không?",
                            "Đồng ý",
                            "Hủy bỏ"
                        ) {
                            listOrderViewModel.updateStatus(order.order_id, 3)
                        }
                    } else {
                        it.showAlertDialogConfirm("Xác nhận", "Bạn không thể hủy đơn hàng này")
                    }
                }
            })
            binding.rcvOrder.apply {
                adapter = listOrderAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(it, RecyclerView.VERTICAL))
            }
        }
    }

    private fun navigateToDetail(order: Order) {
        (context as MainActivity).setCurrentFragment(OrderDetailFragment.newInstance(order.order_id))
        (context as MainActivity).mainViewModel.stackFragment.add(KEY_USER_INFOR_FRAGMENT)
    }

    private fun initObserve() {
        listOrderViewModel = ViewModelProvider(this).get(ListOrderViewModel::class.java)

        listOrderViewModel.listOrder.observe(viewLifecycleOwner) {
            listOrderAdapter.setData(it)
        }
        listOrderViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }
}