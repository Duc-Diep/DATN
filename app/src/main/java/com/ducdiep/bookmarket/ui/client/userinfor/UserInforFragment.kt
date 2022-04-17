package com.ducdiep.bookmarket.ui.client.userinfor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_USER_INFOR_FRAGMENT
import com.ducdiep.bookmarket.databinding.FragmentUserInforBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.client.login.LoginActivity
import com.ducdiep.bookmarket.ui.client.main.MainActivity
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
        binding.root.children.forEach {
            if (it !is ProgressBar){
                it.visibility = View.GONE
            }
        }
        binding.tvIsLogin.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.VISIBLE
    }

    fun loggedIn() {
        binding.root.children.forEach {
            if (it !is ProgressBar){
                it.visibility = View.VISIBLE
            }
        }
        binding.tvIsLogin.visibility = View.GONE
        binding.btnLogin.visibility = View.GONE
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
        userInforViewModel.isLoading.observe(viewLifecycleOwner){
            if (it){
                binding.pbLoading.visibility = View.VISIBLE
            }else{
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun setData(user: User) {
        binding.accName.text = user.full_name
        Glide.with(mContext).load(user.avatar).into(binding.civAvatar)
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
        binding.layoutInfor.setOnClickListener {
            (context as MainActivity).setCurrentFragment(UpdateInforFragment())//sua
            (context as MainActivity).mainViewModel.stackFragment.add(KEY_USER_INFOR_FRAGMENT)
            Log.d("abcc", "${(context as MainActivity).mainViewModel.stackFragment}")
        }
        binding.layoutLogout.setOnClickListener {
            userInforViewModel.logout()
        }
        binding.layoutManage.setOnClickListener {
            startActivity(Intent(context,ManageActivity::class.java))
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