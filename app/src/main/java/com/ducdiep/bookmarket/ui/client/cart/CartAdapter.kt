package com.ducdiep.bookmarket.ui.client.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.databinding.ItemBookCartBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.BookInCart

class CartAdapter(
    var context: Context,
    var listBook: ArrayList<BookInCart>,
    var callbackMinus: (Book) -> Unit,
    var callbackPlus: (Book) -> Unit,
    var callbackDelete: (Book) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(val binding: ItemBookCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(bookInCart: BookInCart) {
            Glide.with(context).load(bookInCart.book.image).into(binding.imgBookCart)
            binding.tvBookName.text = bookInCart.book.name
            binding.tvBookPrice.text = getFormatMoney(bookInCart.book.price) + " vnđ"
            binding.tvNumber.text = bookInCart.number.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding = ItemBookCartBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = CartViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: CartAdapter.CartViewHolder) {
        holder.binding.imgDelete.setOnClickListener {
            callbackDelete.invoke(listBook[holder.layoutPosition].book)
            listBook.removeAt(holder.layoutPosition)
            notifyItemRemoved(holder.layoutPosition)
        }
        holder.binding.imgMinus.setOnClickListener {
            callbackMinus.invoke(listBook[holder.layoutPosition].book)
            val number = holder.binding.tvNumber.text.toString().toInt()
            holder.binding.tvNumber.text =
                if (number > 1) (number - 1).toString() else number.toString()
        }
        holder.binding.imgPlus.setOnClickListener {
            callbackPlus.invoke(listBook[holder.layoutPosition].book)
            val number = holder.binding.tvNumber.text.toString().toInt()
            holder.binding.tvNumber.text = (number + 1).toString()
        }
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.onBind(listBook[position])
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    fun setData(listData: ArrayList<BookInCart>) {
        listBook = listData
        notifyDataSetChanged()
    }

    fun getData():ArrayList<BookInCart>{
        return listBook
    }

    fun clearData(){
        listBook.clear()
        notifyDataSetChanged()
    }

}