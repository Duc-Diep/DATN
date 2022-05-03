package com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.databinding.ItemBookOrderBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.models.BookInCart

class ListBookDetailAdapter(var context: Context, var list: List<BookInCart>) :
    RecyclerView.Adapter<ListBookDetailAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(bookInCart: BookInCart) {
            Glide.with(context).load(bookInCart.book.image).into(binding.imgBookCart)
            binding.tvBookName.text = bookInCart.book.name
            binding.tvBookPrice.text = getFormatMoney(bookInCart.book.price) + "vnđ"
            binding.tvNumber.text = "Số lượng: " + bookInCart.number.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBookOrderBinding.inflate(LayoutInflater.from(context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<BookInCart>) {
        this.list = list
        notifyDataSetChanged()
    }
}