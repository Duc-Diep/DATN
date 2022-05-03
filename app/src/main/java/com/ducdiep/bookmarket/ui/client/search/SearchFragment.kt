package com.ducdiep.bookmarket.ui.client.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.base.*
import com.ducdiep.bookmarket.databinding.FragmentSearchBinding
import com.ducdiep.bookmarket.extensions.viewBinding
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.ui.client.home.bookdetails.BookDetailFragment
import com.ducdiep.bookmarket.ui.client.home.HomeViewModel
import com.ducdiep.bookmarket.ui.client.main.MainActivity

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private lateinit var homeViewModel: HomeViewModel
    private val binding by viewBinding(FragmentSearchBinding::bind)
    lateinit var bookAdapter: BookSearchAdapter
    var title = ""
    var idCategory = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            title = it.getString("title").toString()
            idCategory = it.getString("id").toString()
        }
        initViews()
        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                if (idCategory != "") {
                    homeViewModel.searchDataInCategory(text.toString())
                } else {
                    homeViewModel.searchAllData(text.toString())
                }
            }
        }
    }

    private fun initObserve() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (idCategory != "") {
            homeViewModel.getAllBooksByCategoryId(idCategory, "")
            homeViewModel.listRecommendBooks.observe(viewLifecycleOwner) {
                bookAdapter.setData(it)
            }
            homeViewModel.listSearchByCategory.observe(viewLifecycleOwner) {
                bookAdapter.setData(it)
            }
        } else {
            homeViewModel.listBooks.observe(viewLifecycleOwner) {
                bookAdapter.setData(it)
            }
            homeViewModel.listSearchAllData.observe(viewLifecycleOwner) {
                bookAdapter.setData(it)
            }
        }
    }


    private fun navigateToDetail(book: Book) {
        (context as MainActivity).setCurrentFragment(BookDetailFragment.newInstance(book))
        (context as MainActivity).mainViewModel.stackFragment.add(KEY_HOME_FRAGMENT)
    }

    private fun initViews() {
        binding.tbSearch.setTitle(title)
        context?.let {
            bookAdapter = BookSearchAdapter(it, arrayListOf()) {
                navigateToDetail(it)
            }
            binding.rcvSearch.apply {
                adapter = bookAdapter
                setHasFixedSize(true)
            }
        }
    }

    companion object {
        fun newInstance(title: String, id: String) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putString("id", id)
            }
        }
    }
}