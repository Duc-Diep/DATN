package com.ducdiep.bookmarket.ui.client.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentHomeBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.main.MainViewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var mainViewModel:MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObsever()
    }

    private fun initObsever() {
       mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    companion object {
        fun newInstance() =
            HomeFragment().apply {

            }
    }
}