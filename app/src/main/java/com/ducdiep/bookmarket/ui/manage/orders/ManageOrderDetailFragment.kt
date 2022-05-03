package com.ducdiep.bookmarket.ui.manage.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentManageOrderDetailBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.BookInCart
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail.ListBookDetailAdapter
import com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail.OrderDetailFragment

class ManageOrderDetailFragment:BaseFragment(R.layout.fragment_manage_order_detail) {
    private val binding by viewBinding(FragmentManageOrderDetailBinding::bind)
    lateinit var manageOrderDetailViewModel: ManageOrderDetailViewModel
    lateinit var bookDetailAdapter: ListBookDetailAdapter
    var orderId = ""
    var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId = arguments?.getString("order_id") ?: ""
        userId = arguments?.getString("user_id") ?: ""
        Log.d("abcc", "onViewCreated: $orderId , $userId")
        initViews()
        initObserve()
    }

    private fun initViews() {
        context?.let {
            bookDetailAdapter = ListBookDetailAdapter(it, listOf())
            binding.rcvOrderDetail.apply {
                adapter = bookDetailAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(it, RecyclerView.VERTICAL))
            }
        }
    }

    private fun initObserve() {
        manageOrderDetailViewModel = ViewModelProvider(this).get(ManageOrderDetailViewModel::class.java)
        manageOrderDetailViewModel.getUserInforById(userId)
        manageOrderDetailViewModel.getOrderDetailById(orderId)
        manageOrderDetailViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                setDataUser(it)
            }
        }
        manageOrderDetailViewModel.listOrderDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                manageOrderDetailViewModel.getAllBookInOrder()
            }
        }
        manageOrderDetailViewModel.listBookInCart.observe(viewLifecycleOwner) {
            bookDetailAdapter.setData(it)
            caculatorMoney(it)
        }
    }

    private fun caculatorMoney(arrayList: ArrayList<BookInCart>) {
        view?.let {
            var sum = 0L
            arrayList.forEach {
                sum += it.book.price * it.number
            }
            val discount = sum * 15 / 100
            val sumMoney = sum - discount
            binding.tvTotalAmount.text = "Tổng tạm tính: $sum vnđ"
            binding.tvTotalDiscount.text = "Giảm giá: $discount vnđ"
            binding.tvTotalMoney.text = "Tổng tiền: $sumMoney vnđ"
        }


    }

    private fun setDataUser(user: User) {
        view?.let {
            binding.tvUserAddress.text = "Địa chỉ: ${user.address}"
            binding.tvUserName.text = "Họ tên: ${user.full_name}"
            binding.tvUserPhone.text = "Số điện thoại: ${user.phone}"
        }
    }

    companion object {
        fun newInstance(orderId: String, userId:String) = ManageOrderDetailFragment().apply {
            arguments = Bundle().apply {
                putString("order_id", orderId)
                putString("user_id", userId)
            }
        }
    }


}