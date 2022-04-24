package com.ducdiep.bookmarket.ui.client.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.databinding.ItemSlideBinding
import com.ducdiep.bookmarket.models.Book

class SlideAdapter(var context: Context, var list: List<SlideModel>, var callback: (Book) -> Unit) :
    RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {
    inner class SlideViewHolder(val binding: ItemSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(slideModel: SlideModel) {
            binding.imgSlide.setImageResource(slideModel.resource)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideAdapter.SlideViewHolder {
        val binding = ItemSlideBinding.inflate(LayoutInflater.from(context),parent,false)
        val holder = SlideViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: SlideAdapter.SlideViewHolder) {
        holder.binding.imgSlide.setOnClickListener {
            callback.invoke(list[holder.adapterPosition].book)
        }
    }

    override fun onBindViewHolder(holder: SlideAdapter.SlideViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}