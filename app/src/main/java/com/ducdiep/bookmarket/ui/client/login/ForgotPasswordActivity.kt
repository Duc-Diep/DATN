package com.ducdiep.bookmarket.ui.client.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseActivity
import com.ducdiep.bookmarket.databinding.ActivityForgotPasswordBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class ForgotPasswordActivity : BaseActivity(R.layout.activity_forgot_password) {
    private val binding by viewBinding(ActivityForgotPasswordBinding::inflate)
    lateinit var forgotPassViewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        initObserve()
    }

    private fun initObserve() {
        forgotPassViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        forgotPassViewModel.isSuccess.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "Gửi yêu cầu reset mật khẩu thành công, Vui lòng kiểm tra email",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Gửi yêu cầu reset mật khẩu thất bại, Vui lòng kiểm tra lại chính xác email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        forgotPassViewModel.isLoading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Chưa đúng định dạng email"
                binding.edtEmail.requestFocus()
            } else {
                forgotPassViewModel.sendEmail(email)
            }
        }
    }
}