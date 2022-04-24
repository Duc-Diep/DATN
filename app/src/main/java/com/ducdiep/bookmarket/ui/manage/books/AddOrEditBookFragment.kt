package com.ducdiep.bookmarket.ui.manage.books

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.TABLE_CATEGORIES
import com.ducdiep.bookmarket.databinding.FragmentAddOrEditBookBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.extensions.getSlug
import com.ducdiep.bookmarket.extensions.getTimeNow
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.Category
import com.ducdiep.bookmarket.ui.manage.ManageActivity
import com.ducdiep.bookmarket.ui.manage.categories.ManageCategoryViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask

class AddOrEditBookFragment : BaseFragment(R.layout.fragment_add_or_edit_book) {
    private val binding by viewBinding(FragmentAddOrEditBookBinding::bind)
    private lateinit var manageBookViewModel: ManageBookViewModel
    private lateinit var manageCategoryViewModel: ManageCategoryViewModel
    private lateinit var textWatcher: TextWatcher
    var mode = MODE_ADD
    var uri: Uri? = null

    var book: Book? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        initObserve()
    }

    private fun initObserve() {
        manageBookViewModel = ViewModelProvider(this).get(ManageBookViewModel::class.java)
        manageCategoryViewModel = ViewModelProvider(this).get(ManageCategoryViewModel::class.java)
        manageCategoryViewModel.listCategories.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                setupSpinner(it)
            }
        }
    }

    private fun initViews() {
        book = arguments?.getSerializable("book") as? Book
        book?.let {
            mode = MODE_EDIT
            binding.btnAddOrEdit.setText("Cập nhật sản phẩm")
            setData(it)
        }
    }

    private fun setupSpinner(listData: List<Category>) {
        context?.let { ct ->
            view?.let {
                val spinnerAdapter =
                    SpinnerCategoryAdapter(ct, listData)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCategory.adapter = spinnerAdapter
                if (mode == MODE_EDIT) {
                    binding.spCategory.setSelection(listData.indexOf(listData.find {
                        it.category_id == book?.category_id
                    }))
                } else {
                    binding.spCategory.setSelection(0)
                }
            }
        }
    }

    private fun setData(book: Book) {
        binding.apply {
            Glide.with(requireContext()).load(book.image).into(imgBook)
            edtBookName.setText(book.name)
            edtAuthor.setText(book.author)
            edtNumber.setText(book.current_number.toString())
            edtNumberPage.setText(book.num_of_page.toString())
            edtPrice.setText(book.price.toString())
            edtPublisher.setText(book.publisher)
            edtDescription.setText(book.description)
        }
    }

    private fun initListener() {
        textWatcher = createTextWatcher()
        binding.edtPrice.addTextChangedListener(textWatcher)
        binding.btnAddOrEdit.setOnClickListener {
            if (mode == MODE_ADD) {
                if (uri != null) {
                    uploadBook()
                } else {
                    Toast.makeText(context, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
                }
            } else {//edit
                if (uri != null) {
                    uploadBook()
                } else {
                    val author = binding.edtAuthor.text.toString()
                    val category = (binding.spCategory.selectedItem as Category).category_id
                    val number = binding.edtNumber.text.toString().toInt()
                    val description = binding.edtDescription.text.toString()
                    val name = binding.edtBookName.text.toString()
                    val numberPage = binding.edtNumberPage.text.toString().toInt()
                    val dataPrice = binding.edtPrice.text.toString().replace(".", "")
                    val price = dataPrice.toLong()
                    val publisher = binding.edtPublisher.text.toString()
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("author" to author)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("category_id" to category)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("description" to description)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("name" to name)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("num_of_page" to numberPage)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("current_number" to number)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("price" to price)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("publisher" to publisher)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("updated_at" to getTimeNow())
                    )
                }
                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                (context as ManageActivity).onBackPressed()
            }
        }
        binding.tvChooseImage.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    private var curValue = 0L
    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
        override fun afterTextChanged(s: Editable?) = Unit

        override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {
            val text = s.toString().replace(",", "").replace(".", "")
            if (text.isNullOrEmpty()) return
            binding.edtPrice.removeTextChangedListener(textWatcher)
            curValue = if (text.toLong() in 0..999999999999) text.toLong() else curValue
            binding.edtPrice.setText(getFormatMoney(curValue))
            binding.edtPrice.setSelection(binding.edtPrice.text?.length ?: 0)
            binding.edtPrice.addTextChangedListener(textWatcher)
        }
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            if (it != null) {
                binding.imgBook.setImageURI(it)
                uri = it
            } else {
                Toast.makeText(context, "Chọn ảnh thất bại", Toast.LENGTH_SHORT).show()
            }
        })


    private fun uploadBook() {
        val dialog = ProgressDialog(context)
        dialog.setTitle("Đang cập nhật")
        dialog.show()
        val storage = FirebaseStorage.getInstance()
        val imageref =
            storage.reference.child("images/books/" + getSlug(binding.edtBookName.text.toString()) + ".jpg")
        val uploadTask = imageref.putFile(uri!!)
        uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            dialog.dismiss()
            val downloadUri = taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->

                val author = binding.edtAuthor.text.toString()
                val category = (binding.spCategory.selectedItem as Category).category_id
                val number = binding.edtNumber.text.toString().toInt()
                val description = binding.edtDescription.text.toString()
                val name = binding.edtBookName.text.toString()
                val numberPage = binding.edtNumberPage.text.toString().toInt()
                val dataPrice = binding.edtPrice.text.toString().replace(".", "")
                val price = dataPrice.toLong()
                val publisher = binding.edtPublisher.text.toString()
                if (mode == MODE_ADD) {
                    val addBook = Book(
                        author,
                        "",
                        category,
                        getTimeNow(),
                        number,
                        description,
                        url.toString(),
                        name,
                        numberPage,
                        price,
                        publisher,
                        getTimeNow()
                    )
                    manageBookViewModel.addBook(addBook)
                    Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                    (context as ManageActivity).onBackPressed()
                } else {
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("author" to author)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("category_id" to category)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("description" to description)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("image" to url.toString())
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("name" to name)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("num_of_page" to numberPage)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("current_number" to number)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("price" to price)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("publisher" to publisher)
                    )
                    manageBookViewModel.updateField(
                        book?.book_id.toString(),
                        hashMapOf("updated_at" to getTimeNow())
                    )
                    Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show()
                    (context as ManageActivity).onBackPressed()
                }

                Log.d("TAG", "onSuccess: $uri")
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

    companion object {
        fun newInstance(book: Book?) = AddOrEditBookFragment().apply {
            arguments = Bundle().apply {
                putSerializable("book", book)
            }
        }

        const val MODE_EDIT = 1
        const val MODE_ADD = 0
    }
}