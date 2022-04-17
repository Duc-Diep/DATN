package com.ducdiep.bookmarket.ui.client.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.ActivityRegisterBinding
import com.ducdiep.bookmarket.extensions.*
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.utils.DateTimePickerDialog
import java.util.*

class RegisterActivity : BaseActivity(R.layout.activity_register) {
    private val binding by viewBinding(ActivityRegisterBinding::inflate)
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListener()
        initObserve()
    }

    private fun initObserve() {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.isLoading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

        registerViewModel.isSuccess.observe(this) {
            if (it) {
                finish()
                Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initListener() {
        binding.btnConfirmRegister.setOnClickListener {
            checkValidAndRegister()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.tvDateOfBirth.setOnClickListener {
            showDialogDateTimePicker()
        }
    }

    private fun checkValidAndRegister() {
        val email = binding.edtEmail.text.toString().trim()
        val pass: String = binding.edtPassword.text.toString().trim()
        val repass: String = binding.edtRePassword.text.toString().trim()
        val address: String = binding.edtAddress.text.toString().trim()
        val username: String = binding.edtFullname.text.toString().trim()
        val gender = if (binding.male.isChecked) "Nam" else "Nữ"
        val avatar = if (binding.male.isChecked) IMG_DEFAULT_MALE else IMG_DEFAULT_FEMALE
        val dob = binding.tvDateOfBirth.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Chưa đúng định dạng email")
            binding.edtEmail.requestFocus()
            return
        }
        if (pass.length < 6) {
            binding.edtPassword.setError("Mật khẩu không được nhỏ hơn 6 ký tự")
            binding.edtPassword.requestFocus()
            return
        }
        if (repass != pass) {
            binding.edtRePassword.setError("Mật khẩu không trùng nhau")
            binding.edtRePassword.requestFocus()
            return
        }
        if (username.isEmpty()) {
            binding.edtFullname.setError("Tên không được để trống")
            binding.edtFullname.requestFocus()
            return
        }
        val user = User(
            address,
            avatar,
            getTimeNow(),
            email,
            username,
            pass,
            gender,
            "",
            ROLE_CUSTOMER,
            getTimeNow(),
            "",
            dob, 1
        )
        registerViewModel.register(user)
    }

    private fun showDialogDateTimePicker() {
        val datePicker = DateTimePickerDialog.getInstance(Calendar.getInstance())
        datePicker.setOnDateChange { calendar ->
            binding.tvDateOfBirth.text = calendar.clearTime().parseToString(DATE_FORMAT)
        }
        datePicker.show(supportFragmentManager, null)
    }

    private fun initViews() {
        binding.tvDateOfBirth.text = Calendar.getInstance().clearTime().parseToString(DATE_FORMAT)
    }

}