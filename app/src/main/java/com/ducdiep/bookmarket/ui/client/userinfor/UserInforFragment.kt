package com.ducdiep.bookmarket.ui.client.userinfor

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentUserInforBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.login.LoginActivity

class UserInforFragment : BaseFragment(R.layout.fragment_user_infor) {

    private val binding by viewBinding(FragmentUserInforBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLoginAndLogout.setOnClickListener {
            startActivity(Intent(context,LoginActivity::class.java))
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            UserInforFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}