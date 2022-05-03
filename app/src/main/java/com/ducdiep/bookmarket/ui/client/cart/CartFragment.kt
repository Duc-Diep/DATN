package com.ducdiep.bookmarket.ui.client.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentCartBinding
import com.ducdiep.bookmarket.extensions.getTimeNow
import com.ducdiep.bookmarket.extensions.showDialogConfirm
import com.ducdiep.bookmarket.extensions.showDialogConfirmOrder
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Order
import com.ducdiep.bookmarket.models.OrderDetail
import com.ducdiep.bookmarket.ui.client.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class CartFragment : BaseFragment(R.layout.fragment_cart) {
    private val binding by viewBinding(FragmentCartBinding::bind)
    lateinit var cartViewModel: CartViewModel
    lateinit var cartAdapter: CartAdapter
    var userId = FirebaseAuth.getInstance().uid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.btnOrder.setOnClickListener {
            if (userId != null) {
                if (cartViewModel.user != null) {
                    context?.showDialogConfirmOrder(cartViewModel.user!!) {
                        var sum = 0L
                        cartAdapter.getData().forEach {
                            sum += it.book.price * it.number
                        }
                        val discount = sum * 15 / 100
                        val order =
                            Order(getTimeNow(), discount, "", sum, getTimeNow(), userId!!, 0)
                        var listOrder = arrayListOf<OrderDetail>()
                        cartAdapter.getData().forEach {
                            listOrder.add(
                                OrderDetail(
                                    it.book.book_id, getTimeNow(), "", "", it.number, getTimeNow()
                                )
                            )
                        }
                        cartViewModel.orderCart(order, listOrder)
                        cartAdapter.clearData()
                        binding.btnOrder.visibility = View.GONE
                        binding.tvNone.visibility = View.VISIBLE
                        Toast.makeText(context, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                context?.showDialogConfirm(
                    "Bạn chưa đăng nhập, Vui lòng đăng nhập để mua hàng",
                    "Đồng ý",
                    "Hủy bỏ"
                ) {
                    startActivity(Intent(context, LoginActivity::class.java))
                }
            }

        }
    }

    private fun initViews() {
        context?.let {
            cartAdapter = CartAdapter(it, arrayListOf(), {
                cartViewModel.minusNumber(it.book_id)
            }, {
                cartViewModel.plusNumber(it.book_id)
            }, {
                cartViewModel.deleteItem(it.book_id)
                if (cartAdapter.getData().size == 1) {
                    binding.btnOrder.visibility = View.GONE
                    binding.tvNone.visibility = View.VISIBLE
                }
            })

            binding.rcvCart.apply {
                adapter = cartAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            }
        }
    }

    private fun initObserve() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.listBookInCart.observe(viewLifecycleOwner) {
            view?.let { view ->
                if (it.isNotEmpty()) {
                    cartAdapter.setData(it)
                    binding.btnOrder.visibility = View.VISIBLE
                    binding.tvNone.visibility = View.GONE
                } else {
                    binding.btnOrder.visibility = View.GONE
                    binding.tvNone.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun newInstance(): CartFragment = CartFragment().apply {

        }
    }
}