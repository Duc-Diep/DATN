package com.ducdiep.bookmarket.ui.manage.accounts

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentAccountDetailsBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.manage.ManageActivity

class AccountDetailsFragment : BaseFragment(R.layout.fragment_account_details) {
    private val binding by viewBinding(FragmentAccountDetailsBinding::bind)
    lateinit var manageAccountViewModel: ManageAccountViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            (context as ManageActivity).onBackPressed()
        }
    }

    private fun initObserve() {
        val id =
            arguments?.getString("id")
        manageAccountViewModel = ViewModelProvider(this).get(ManageAccountViewModel::class.java)
        id?.let { manageAccountViewModel.getUserById(it) }
        manageAccountViewModel.userDetails.observe(viewLifecycleOwner){
            if (it!=null){
                setData(it)
            }
        }
    }

    private fun setData(user: User) {
        binding.tvEmail.text = user.email
        Glide.with(requireContext()).load(user.avatar).into(binding.imgAvatarUser)
        binding.edtAddress.text = user.address
        binding.edtFullname.text = user.full_name
        binding.tvDateOfBirth.text = user.date_of_birth
        binding.edtPhone.text = user.phone
    }

    companion object {
        fun newInstance(id: String) = AccountDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("id", id)
            }
        }
    }
}