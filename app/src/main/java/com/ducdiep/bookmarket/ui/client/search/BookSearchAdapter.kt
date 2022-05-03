package com.ducdiep.bookmarket.ui.client.search

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.databinding.ItemBookSearchBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import com.ducdiep.bookmarket.models.Book

class BookSearchAdapter(var context: Context, var listBook: List<Book>, var callback: (Book) -> Unit) :
    RecyclerView.Adapter<BookSearchAdapter.BookViewHolder>() {
    inner class BookViewHolder(val binding: ItemBookSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(book: Book) {
            binding.tvBookName.text = book.name
            binding.tvBookName.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.tvBookName.setHorizontallyScrolling(true)
            binding.tvBookName.isSelected = true
            binding.tvBookName.marqueeRepeatLimit = -1
            Glide.with(context).load(book.image).into(binding.imgBook)
            binding.tvPrice.text = getFormatMoney(book.price) + " vnđ"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = BookViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: BookSearchAdapter.BookViewHolder) {
        holder.binding.rlRoot.setOnClickListener {
            callback.invoke(listBook[holder.layoutPosition])
        }
    }

    fun setData(listBook: List<Book>){
        this.listBook = listBook
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.onBind(listBook[position])
    }

    override fun getItemCount(): Int {
        return listBook.size
    }
}