package com.ducdiep.bookmarket.ui.client.userinfor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_USER_INFOR_FRAGMENT
import com.ducdiep.bookmarket.base.ROLE_ADMIN
import com.ducdiep.bookmarket.databinding.FragmentUserInforBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.client.login.LoginActivity
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.ui.client.userinfor.order.listorder.ListOrderFragment
import com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail.OrderDetailFragment
import com.ducdiep.bookmarket.ui.manage.ManageActivity

class UserInforFragment : BaseFragment(R.layout.fragment_user_infor) {

    private val binding by viewBinding(FragmentUserInforBinding::bind)
    private lateinit var userInforViewModel: UserInforViewModel
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserve()
        initViews()
    }

    private fun initViews() {

    }

    fun unLogin() {
        view?.let {
            binding.root.children.forEach {
                it.visibility = View.GONE
            }
            binding.tvIsLogin.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }

    fun loggedIn() {
        view?.let {
            binding.root.children.forEach {
                it.visibility = View.VISIBLE
            }
            binding.rlLoading.visibility = View.VISIBLE
            binding.tvIsLogin.visibility = View.GONE
            binding.btnLogin.visibility = View.GONE
        }
    }


    private fun initObserve() {
        userInforViewModel = ViewModelProvider(this).get(UserInforViewModel::class.java)
        userInforViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                setData(it)
            }
        }
        userInforViewModel.firebaseUser.observe(viewLifecycleOwner) {
            if (it != null) {
                loggedIn()
            } else {
                unLogin()
            }
        }
        userInforViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.rlLoading.visibility = View.VISIBLE
            } else {
                binding.rlLoading.visibility = View.GONE
            }
        }
    }

    private fun setData(user: User) {
        view?.let {
            binding.accName.text = user.full_name
            Glide.with(mContext).load(user.avatar).into(binding.civAvatar)
            binding.layoutManage.visibility =
                if (user.role == ROLE_ADMIN) View.VISIBLE else View.GONE
        }
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        binding.layoutInfor.setOnClickListener {
            (context as MainActivity).setCurrentFragment(UpdateInforFragment())//sua
            (context as MainActivity).mainViewModel.stackFragment.add(KEY_USER_INFOR_FRAGMENT)
        }
        binding.layoutLogout.setOnClickListener {
            userInforViewModel.logout()
        }
        binding.layoutManage.setOnClickListener {
            startActivity(Intent(context, ManageActivity::class.java))
        }
        binding.layoutOrder.setOnClickListener {
            (context as MainActivity).setCurrentFragment(ListOrderFragment())
            (context as MainActivity).mainViewModel.stackFragment.add(KEY_USER_INFOR_FRAGMENT)
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            UserInforFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onPause() {
        super.onPause()
        binding.pbLoading.visibility = View.GONE
    }
}