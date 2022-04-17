package com.ducdiep.bookmarket.ui.manage.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentManageOrderBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class ManageOrderFragment:BaseFragment(R.layout.fragment_manage_order) {
    private val binding by viewBinding(FragmentManageOrderBinding::bind)
    lateinit var manageOrderViewModel: ManageOrderViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initObserve() {
        manageOrderViewModel = ViewModelProvider(this).get(ManageOrderViewModel::class.java)
    }
}