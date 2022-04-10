package com.ducdiep.bookmarket.ui.client.home

import android.os.Bundle
import android.view.View
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentHomeBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() =
            HomeFragment().apply {

            }
    }
}