package com.ducdiep.bookmarket.ui.client.cart

import android.os.Bundle
import android.view.View
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.FragmentCartBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class CartFragment : BaseFragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance(): CartFragment = CartFragment().apply {

        }
    }
}