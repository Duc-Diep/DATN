package com.ducdiep.bookmarket.ui.client.category

import android.os.Bundle
import android.view.View
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.FragmentCategoryBinding
import com.ducdiep.bookmarket.extensions.viewBinding

class CategoryFragment : BaseFragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        fun newInstance() =
            CategoryFragment().apply {

            }
    }
}