package com.ducdiep.bookmarket.ui.manage.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.databinding.ItemManageAccountBinding
import com.ducdiep.bookmarket.models.User

class ManageAccountAdapter(
    var context: Context,
    var listUser: List<User>,
    var callBackBlock: (String, Int) -> Unit,
    var callBackClickItem: (String) -> Unit
) : RecyclerView.Adapter<ManageAccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: ItemManageAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(context).load(user.avatar).into(binding.imgAvatar)
            binding.tvEmail.text = user.email
            if (user.active == 1) {
                binding.btnBlock.setImageResource(R.drawable.ic_baseline_check_circle_24)
            } else {
                binding.btnBlock.setImageResource(R.drawable.ic_baseline_block_24)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageAccountAdapter.AccountViewHolder {
        val binding = ItemManageAccountBinding.inflate(LayoutInflater.from(context))
        val holder = AccountViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: AccountViewHolder) {
        holder.binding.btnBlock.setOnClickListener {
            callBackBlock.invoke(
                listUser[holder.adapterPosition].user_id,
                listUser[holder.adapterPosition].active
            )
        }
        holder.itemView.setOnClickListener {
            callBackClickItem.invoke(listUser[holder.adapterPosition].user_id)
        }
    }

    override fun onBindViewHolder(holder: ManageAccountAdapter.AccountViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun setData(listUser: List<User>){
        this.listUser = listUser.reversed()
        notifyDataSetChanged()
    }
}