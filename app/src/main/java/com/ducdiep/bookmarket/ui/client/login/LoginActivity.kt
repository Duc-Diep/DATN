package com.ducdiep.bookmarket.ui.client.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseActivity
import com.ducdiep.bookmarket.databinding.ActivityLoginBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.main.MainActivity

class LoginActivity : BaseActivity(R.layout.activity_login) {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    lateinit var loginViewModel:LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        initObserve()
    }

    private fun initObserve() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.isLoading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
        loginViewModel.isSuccess.observe(this){
            if (it){
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
                startActivity(intent)
            }else{
                Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initListener() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener { 
            logIn()
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    private fun logIn() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.getText().toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Chưa đúng định dạng email"
            binding.edtEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            binding.edtPassword.error = "Mật khẩu không được để trống"
            binding.edtPassword.requestFocus()
            return
        }
        loginViewModel.logIn(email, password)
    }
}