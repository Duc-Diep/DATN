package com.ducdiep.bookmarket.ui.client.login

import android.content.Intent
import android.os.Bundle
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.ActivityLoginBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class LoginActivity : BaseActivity(R.layout.activity_login) {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.tvRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}