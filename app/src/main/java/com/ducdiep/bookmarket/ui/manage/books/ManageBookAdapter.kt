package com.ducdiep.bookmarket.ui.manage.books

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.databinding.ItemManageBookBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.models.Book

class ManageBookAdapter(
    var context: Context,
    var listBooks: List<Book>,
    var callbackDelete: (String) -> Unit,
    var callbackClickItem: (String) -> Unit
) : RecyclerView.Adapter<ManageBookAdapter.BookViewHolder>() {
    inner class BookViewHolder(val binding: ItemManageBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageBookAdapter.BookViewHolder {
        var binding = ItemManageBookBinding.inflate(LayoutInflater.from(context))
        val holder = BookViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: ManageBookAdapter.BookViewHolder) {
        holder.binding.tvRemove.setOnClickListener {
            callbackDelete.invoke(listBooks[holder.layoutPosition].book_id)
        }
        holder.itemView.setOnClickListener{
            callbackClickItem.invoke(listBooks[holder.layoutPosition].book_id)
        }
    }

    override fun onBindViewHolder(holder: ManageBookAdapter.BookViewHolder, position: Int) {
        Glide.with(context).load(listBooks[position].image).into(holder.binding.imgBookItem)
        holder.binding.tvBookNameItem.text = listBooks[position].name
        holder.binding.tvBookPriceItem.text = getFormatMoney(listBooks[position].price)
    }

    override fun getItemCount(): Int {
        return listBooks.size
    }
}