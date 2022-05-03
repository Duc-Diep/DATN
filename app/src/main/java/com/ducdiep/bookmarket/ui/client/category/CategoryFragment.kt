package com.ducdiep.bookmarket.ui.client.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.BaseFragment
import com.ducdiep.bookmarket.base.KEY_CATEGORY_FRAGMENT
import com.ducdiep.bookmarket.databinding.FragmentCategoryBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.ui.client.main.MainActivity
import com.ducdiep.bookmarket.ui.client.search.SearchFragment

class CategoryFragment : BaseFragment(R.layout.fragment_category) {
    private val binding by viewBinding(FragmentCategoryBinding::bind)
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryAdapter: CategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserve()
    }

    private fun initViews() {
        context?.let {
            categoryAdapter = CategoryAdapter(it, listOf()) { category ->
                navigateToSearch(category.name,category.category_id)
            }
            binding.rcvCategory.apply {
                adapter = categoryAdapter
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(it,RecyclerView.VERTICAL))
            }
        }
    }

    private fun navigateToSearch(title: String, id: String) {
        (context as MainActivity).setCurrentFragment(SearchFragment.newInstance(title, id))
        (context as MainActivity).mainViewModel.stackFragment.add(KEY_CATEGORY_FRAGMENT)
    }

    private fun initObserve() {
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.listCategories.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                categoryAdapter.setData(it)
            }
        }
        categoryViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance() =
            CategoryFragment().apply {

            }
    }
}