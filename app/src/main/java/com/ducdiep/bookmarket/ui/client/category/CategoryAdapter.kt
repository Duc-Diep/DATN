package com.ducdiep.bookmarket.ui.client.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.databinding.ItemCategoryBinding
import com.ducdiep.bookmarket.databinding.ItemManageCategoryBinding
import com.ducdiep.bookmarket.models.Category

class CategoryAdapter(
    var context: Context,
    var listCategory: List<Category>,
    var callbackClickItem: (Category) -> Unit,
):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val binding:ItemCategoryBinding):RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category){
            binding.tvCategoryItem.text = category.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context))
        val holder = CategoryViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: CategoryViewHolder) {
        holder.binding.rlBody.setOnClickListener{
            callbackClickItem.invoke(listCategory[holder.layoutPosition])
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(listCategory[position])
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun setData(list: List<Category>){
        listCategory = list
        notifyDataSetChanged()
    }
}