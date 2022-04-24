package com.ducdiep.bookmarket.ui.manage.books

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ducdiep.bookmarket.databinding.ItemSpinnerBinding
import com.ducdiep.bookmarket.models.Category

class SpinnerCategoryAdapter (context: Context, listData: List<Category>) :
    ArrayAdapter<Category>(context, 0, listData) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    private fun createItemView(position: Int, parent: ViewGroup): View {
        val data = getItem(position)
        val binding =
            ItemSpinnerBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.tvCategory.text = data?.name
        return binding.root
    }
}