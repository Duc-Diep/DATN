package com.ducdiep.bookmarket.ui.client.userinfor.order.listorder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.base.STATUS_RECEIVED
import com.ducdiep.bookmarket.base.STATUS_SHIPPED
import com.ducdiep.bookmarket.base.STATUS_WAITING
import com.ducdiep.bookmarket.databinding.ItemListOrderBinding
import com.ducdiep.bookmarket.models.Order

class ListOrderAdapter(var context: Context, var list: List<Order>, var callback: (Order) -> Unit,var callbackCancel:(Order)->Unit) :
    RecyclerView.Adapter<ListOrderAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemListOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(order: Order) {
            binding.tvOrderId.text = "${order.order_id}"
            binding.tvTimeOrder.text = order.created_at
            binding.tvStatusOrder.text = when (order.status) {
                STATUS_WAITING -> "Đang chờ"
                STATUS_RECEIVED -> "Đã nhận"
                STATUS_SHIPPED -> "Đang giao"
                else -> "Đã hủy"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListOrderBinding.inflate(LayoutInflater.from(context))
        val holder = ViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: ViewHolder) {
        holder.binding.rlBody.setOnClickListener {
            callback.invoke(list[holder.layoutPosition])
        }
        holder.binding.tvCancelOrder.setOnClickListener {
            callbackCancel.invoke(list[holder.layoutPosition])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<Order>) {
        this.list = list.reversed()
        notifyDataSetChanged()
    }
}