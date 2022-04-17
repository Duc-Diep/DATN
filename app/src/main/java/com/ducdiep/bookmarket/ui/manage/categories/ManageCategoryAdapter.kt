package com.ducdiep.bookmarket.ui.manage.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.databinding.ItemManageCategoryBinding
import com.ducdiep.bookmarket.models.Category

class ManageCategoryAdapter(
    var context: Context,
    var listCategory: List<Category>,
    var callbackClickItem: (Category) -> Unit,
    var callbackRemove: (Category) -> Unit
) : RecyclerView.Adapter<ManageCategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: ItemManageCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category) {
            binding.tvCategoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageCategoryAdapter.CategoryViewHolder {
        val binding = ItemManageCategoryBinding.inflate(LayoutInflater.from(context))
        val holder = CategoryViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: ManageCategoryAdapter.CategoryViewHolder) {
        holder.binding.tvRemove.setOnClickListener {
            callbackRemove.invoke(listCategory[holder.adapterPosition])
        }
        holder.binding.rlBody.setOnClickListener{
            callbackClickItem.invoke(listCategory[holder.adapterPosition])
        }
    }

    override fun onBindViewHolder(holder: ManageCategoryAdapter.CategoryViewHolder, position: Int) {
        holder.onBind(listCategory[position])
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }
}