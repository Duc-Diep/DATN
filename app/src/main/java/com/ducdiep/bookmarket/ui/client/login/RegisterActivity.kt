package com.ducdiep.bookmarket.ui.client.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.ActivityRegisterBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User

class RegisterActivity : BaseActivity(R.layout.activity_register) {
    private val binding by viewBinding(ActivityRegisterBinding::inflate)
    var registerViewModel: RegisterViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListener()
    }

    private fun initListener() {
        binding.btnConfirmRegister.setOnClickListener {
            checkValidAndRegister()
            registerViewModel.register()
        }
    }

    private fun checkValidAndRegister() {
        val email = binding.edtEmail.text.toString().trim()
        val pass: String = binding.edtPassword.text.toString().trim()
        val repass: String = binding.edtRePassword.text.toString().trim()
        val address: String = binding.edtAddress.text.toString().trim()
        val username: String = binding.edtFullname.text.toString().trim()
        val gender = if (binding.male.isChecked) "Nam" else "Nữ"
        var user = User(address,"")
    }

    private fun initViews() {

    }

}