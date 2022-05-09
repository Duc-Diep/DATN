package com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentOrderDetailBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.BookInCart
import com.ducdiep.bookmarket.models.User

class OrderDetailFragment : BaseFragment(R.layout.fragment_order_detail) {
    private val binding by viewBinding(FragmentOrderDetailBinding::bind)
    lateinit var orderDetailViewModel: OrderDetailViewModel
    lateinit var bookDetailAdapter: ListBookDetailAdapter
    var orderId = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId = arguments?.getString("order_id") ?: ""
        Log.d("abcc", "onViewCreated: $orderId")
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
        orderDetailViewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
        orderDetailViewModel.getOrderDetailById(orderId)
        orderDetailViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                setDataUser(it)
            }
        }
        orderDetailViewModel.listOrderDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                orderDetailViewModel.getAllBookInOrder()
            }
        }
        orderDetailViewModel.listBookInCart.observe(viewLifecycleOwner) {
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
            binding.tvTotalAmount.text = "Tổng tạm tính: ${getFormatMoney(sum)} vnđ"
            binding.tvTotalDiscount.text = "Giảm giá: ${getFormatMoney(discount)}"
            binding.tvTotalMoney.text = "Tổng tiền: ${getFormatMoney(sumMoney)}"
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
        fun newInstance(id: String) = OrderDetailFragment().apply {
            arguments = Bundle().apply {
                putString("order_id", id)
            }
        }
    }
}