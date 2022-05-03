package com.ducdiep.bookmarket.ui.client.userinfor

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.DATE_FORMAT
import com.ducdiep.bookmarket.databinding.FragmentChangeInforBinding
import com.ducdiep.bookmarket.extensions.clearTime
import com.ducdiep.bookmarket.extensions.parseToString
import com.ducdiep.bookmarket.extensions.showDialogChangePass
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.User
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.utils.DateTimePickerDialog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import java.util.*


class UpdateInforFragment : BaseFragment(R.layout.fragment_change_infor) {
    private val binding by viewBinding(FragmentChangeInforBinding::bind)
    private lateinit var userInforViewModel: UserInforViewModel

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            if (it != null) {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                uploadImage(it)
            } else {
                Toast.makeText(context, "Chọn ảnh thất bại", Toast.LENGTH_SHORT).show()
            }
        })

    private fun uploadImage(uri: Uri) {
        val dialog = ProgressDialog(context)
        dialog.setTitle("Đang tải")
        dialog.show()
        val storage = FirebaseStorage.getInstance()
        val imageref =
            storage.reference.child("images/user_avatar/" + userInforViewModel.firebaseUser.value?.uid + ".jpg")
        val uploadTask = imageref.putFile(uri)
        uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            dialog.dismiss()
            val downloadUri = taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                updateImage(uri)
            }
        }).addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot> { snapshot ->
            val percent = (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat()
            dialog.setMessage("Đang tải: " + percent.toInt() + "%")
        }).addOnFailureListener(OnFailureListener {
            dialog.dismiss()
            Toast.makeText(
                context,
                "Fail to upload $it",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    fun updateImage(uri: Uri) {
        val hm = hashMapOf<String, Any>("avatar" to uri.toString())
        userInforViewModel.updateField(hm)
        Glide.with(requireContext()).load(uri.toString()).into(binding.imgAvatarUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserve()
    }

    private fun initObserve() {
        userInforViewModel = ViewModelProvider(this).get(UserInforViewModel::class.java)
        userInforViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                setData(it)
            }
        }
    }

    private fun setData(user: User) {
        binding.tvEmail.text = user.email
        Glide.with(requireContext()).load(user.avatar).into(binding.imgAvatarUser)
        binding.edtAddress.setText(user.address)
        binding.edtFullname.setText(user.full_name)
        binding.tvDateOfBirth.text = user.date_of_birth
        binding.edtPhone.setText(user.phone)
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            (requireContext() as MainActivity).onBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            context?.showDialogChangePass()
        }
        binding.tvDateOfBirth.setOnClickListener {
            showDialogDateTimePicker()
        }
        binding.edtPhone.setOnFocusChangeListener { v, b ->
            if (!b) {
                val hm = hashMapOf<String, Any>("phone" to binding.edtPhone.text.toString())
                userInforViewModel.updateField(hm)
            }
        }
        binding.tvDateOfBirth.doOnTextChanged { _, _, _, _ ->
            val hm =
                hashMapOf<String, Any>("date_of_birth" to binding.tvDateOfBirth.text.toString())
            userInforViewModel.updateField(hm)
        }

        binding.edtFullname.setOnFocusChangeListener { view, b ->
            if (!b) {
                val hm =
                    hashMapOf<String, Any>("full_name" to binding.edtFullname.text.toString())
                userInforViewModel.updateField(hm)
            }
        }
        binding.edtAddress.setOnFocusChangeListener { view, b ->
            if (!b) {
                val hm =
                    hashMapOf<String, Any>("address" to binding.edtAddress.text.toString())
                userInforViewModel.updateField(hm)
            }
        }
        binding.imgChooseImg.setOnClickListener {
            requestPermission()
        }
    }

    private fun showDialogDateTimePicker() {
        val datePicker = DateTimePickerDialog.getInstance(Calendar.getInstance())
        datePicker.setOnDateChange { calendar ->
            binding.tvDateOfBirth.text = calendar.clearTime().parseToString(DATE_FORMAT)
        }
        datePicker.show(parentFragmentManager, null)
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            getImage.launch("image/*")
            return
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                getImage.launch("image/*")
            }
        }

    }

    private val mPermissionResult = registerForActivityResult(
        RequestPermission()
    ) { result ->
        if (result) {
            Toast.makeText(
                context,
                "Access permission read external success",
                Toast.LENGTH_SHORT
            ).show()
            getImage.launch("image/*")
        } else {
            Toast.makeText(
                context,
                "Access permission read external denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}