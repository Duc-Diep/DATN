package com.ducdiep.bookmarket.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.databinding.LayoutDialogChangePassBinding
import com.ducdiep.bookmarket.databinding.LayoutDialogConfirmBinding
import com.ducdiep.bookmarket.databinding.LayoutDialogUserBinding
import com.ducdiep.bookmarket.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun Context.showDialogChangePass() {
    val binding = LayoutDialogChangePassBinding.inflate(LayoutInflater.from(this))
    val builder = AlertDialog.Builder(this).apply {
        setView(binding.root)
    }
    val dialog = builder.create().apply {
        setCanceledOnTouchOutside(false)
    }
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    binding.btnCancel.setOnClickListener {
        dialog.dismiss()
    }
    binding.btnChangePass.setOnClickListener {
        binding.apply {
            val currentPass: String = edtCurrentPass.text.toString().trim { it <= ' ' }
            val newPass: String = edtNewPass.text.toString().trim { it <= ' ' }
            val confirmPass: String = edtConfirmPass.text.toString().trim { it <= ' ' }

            if (currentPass.isEmpty()) {
                edtCurrentPass.setError("Mật khẩu không được để trống")
                edtCurrentPass.requestFocus()
                return@setOnClickListener
            }
            if (newPass.length < 6) {
                edtNewPass.setError("Độ dài mật khẩu > 6")
                edtNewPass.requestFocus()
                return@setOnClickListener
            }
            if (confirmPass != newPass) {
                edtConfirmPass.setError("Xác nhận lại mật khẩu không đúng")
                edtConfirmPass.requestFocus()
                return@setOnClickListener
            }
            processBar.visibility = View.VISIBLE
            var credential =
                EmailAuthProvider.getCredential(firebaseUser?.email.toString(), currentPass)
            firebaseUser?.reauthenticate(credential)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseUser.updatePassword(newPass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Cập nhật mật khẩu thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Cập nhật mật khẩu không thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Mật khẩu hiện tại không đúng",
                        Toast.LENGTH_SHORT
                    ).show()
                    processBar.visibility = View.GONE
                }
            }
        }
    }
    dialog.show()
}

fun Context.showDialogConfirm(
    message: String,
    positiveBtnStr: String,
    negativeBtnStr: String,
    callback: () -> Unit = {},
) {
    val style = R.style.AlertDialogThemeForDelete
    val builder = MaterialAlertDialogBuilder(this, style)
    builder.apply {
        setMessage(message)
        setPositiveButton(positiveBtnStr) { dialog, _ ->
            callback()
            dialog.dismiss()
        }
        setNegativeButton(negativeBtnStr) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)

    }
    val alert = builder.show()
    val positive = alert.getButton(AlertDialog.BUTTON_POSITIVE)
    val negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE)
    negative.isSingleLine = true
    positive.isSingleLine = true
}

fun Context.showAlertDialog(title: String?, message: String?, callback: () -> Unit = {}) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { _, _ ->
            callback()
        }
        setNegativeButton("Hủy bỏ") { _, _ ->

        }
        show()
    }
}


fun Context.showDialogInputUser(
    message: String = "",
    text: String = "",
    doneClickCallBack: (str: String) -> Unit = {},
) {
    val binding = LayoutDialogUserBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    val window: Window? = dialog.window
    window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.gravity = Gravity.CENTER
    binding.edtDialogInput.requestFocus()
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    binding.tvMessage.text = message
    binding.edtDialogInput.doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrEmpty()) {
            binding.ivRemoveText.visibility = View.GONE
            binding.tvDialogDone.setTextColor(
                ContextCompat.getColor(this, R.color.grey)
            )
        } else {
            binding.tvDialogDone.isEnabled = true
            binding.ivRemoveText.visibility = View.VISIBLE
            binding.tvDialogDone.setTextColor(ContextCompat.getColor(this, R.color.blue_teal))
        }
    }
    binding.edtDialogInput.setText(text)
    binding.edtDialogInput.setSelection(text.length)
    binding.ivRemoveText.setOnClickListener {
        binding.edtDialogInput.setText("")
    }
    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }
    binding.tvDialogDone.setOnClickListener {
        doneClickCallBack(binding.edtDialogInput.text.toString())
        dialog.dismiss()
    }
    dialog.show()
}


fun Context.showDialogConfirmOrder(user: User, callback: () -> Unit) {
    val binding = LayoutDialogConfirmBinding.inflate(LayoutInflater.from(this))
    val builder = AlertDialog.Builder(this).apply {
        setView(binding.root)
    }
    val dialog = builder.create().apply {
        setCanceledOnTouchOutside(false)
    }
    binding.edtAddress.setText(user.address)
    binding.edtFullName.setText(user.full_name)
    binding.edtPhone.setText(user.phone)

    var firebaseUser = FirebaseAuth.getInstance().currentUser
    val data =
        FirebaseDatabase.getInstance().getReference(TABLE_USERS).child(firebaseUser?.uid.toString())
    binding.btnCancel.setOnClickListener {
        dialog.dismiss()
    }
    binding.btnConfirm.setOnClickListener {
        val hmPhone = hashMapOf<String, Any>("phone" to binding.edtPhone.text.toString())
        val hmName = hashMapOf<String, Any>("full_name" to binding.edtFullName.text.toString())
        val hmAddress = hashMapOf<String, Any>("address" to binding.edtAddress.text.toString())
        data.updateChildren(hmAddress)
        data.updateChildren(hmPhone)
        data.updateChildren(hmName)
        callback.invoke()
        dialog.dismiss()
    }
    dialog.show()
}