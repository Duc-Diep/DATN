package com.ducdiep.bookmarket.ui.manage.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.databinding.FragmentManageCategoryBinding
import com.ducdiep.bookmarket.extensions.getTimeNow
import com.ducdiep.bookmarket.extensions.showDialogConfirm
import com.ducdiep.bookmarket.extensions.showDialogInputUser
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Category

class ManageCategoryFragment : BaseFragment(R.layout.fragment_manage_category) {
    private val binding by viewBinding(FragmentManageCategoryBinding::bind)
    lateinit var manageCategoryViewModel: ManageCategoryViewModel
    lateinit var categoryAdapter: ManageCategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.btnAddCategory.setOnClickListener {
            context?.showDialogInputUser("Thêm mới danh mục") {
                manageCategoryViewModel.addCategory(Category("", getTimeNow(), it, getTimeNow()))
                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initObserve() {
        manageCategoryViewModel = ViewModelProvider(this).get(ManageCategoryViewModel::class.java)
        manageCategoryViewModel.listCategories.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setupAdapter(it)
            }
        }
        manageCategoryViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun setupAdapter(list: ArrayList<Category>) {
        categoryAdapter = ManageCategoryAdapter(requireContext(), list, {
            Log.d("click", "setupAdapter: ")
            val id = it.category_id
            context?.showDialogInputUser("Sửa danh mục", it.name) { categoryName ->
                manageCategoryViewModel.updateField(id, hashMapOf("name" to categoryName))
                manageCategoryViewModel.updateField(id, hashMapOf("updated_at" to getTimeNow()))
            }
        }, {
            context?.showDialogConfirm(
                "Bạn có chắc chắn muốn xóa sản phẩm này không?",
                "Đồng ý",
                "Hủy bỏ"
            ) {
                manageCategoryViewModel.removeById(it.category_id)
            }
        })

        binding.rcvManageCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }
}