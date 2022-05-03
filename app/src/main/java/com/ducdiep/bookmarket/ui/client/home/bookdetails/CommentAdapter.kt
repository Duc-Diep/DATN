package com.ducdiep.bookmarket.ui.client.home.bookdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.databinding.ItemRatingBinding
import com.ducdiep.bookmarket.models.Comment

class CommentAdapter(var context: Context, var list: List<Comment>):RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    inner class CommentViewHolder(val binding:ItemRatingBinding):RecyclerView.ViewHolder(binding.root) {
        fun onBind(comment: Comment){
            Glide.with(context).load(comment.user.avatar).into(binding.imgAvatar)
            binding.rbStar.rating = comment.productRating.star
            binding.tvComment.text = comment.productRating.comment
            binding.tvUserName.text = comment.user.full_name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(context))
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setData(list: List<Comment>){
        this.list = list.reversed()
        notifyDataSetChanged()
    }
}