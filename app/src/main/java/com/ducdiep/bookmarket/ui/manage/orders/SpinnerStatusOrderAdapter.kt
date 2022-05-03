package com.ducdiep.bookmarket.ui.manage.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ducdiep.bookmarket.databinding.ItemSpinnerStatusBinding

class SpinnerStatusOrderAdapter (context: Context, listData: List<String>) :
    ArrayAdapter<String>(context, 0, listData) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    private fun createItemView(position: Int, parent: ViewGroup): View {
        val data = getItem(position)
        val binding =
            ItemSpinnerStatusBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.tvCategory.text = data
        return binding.root
    }
}