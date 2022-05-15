package com.ducdiep.bookmarket.ui.manage.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.databinding.ItemManageOrderBinding
import com.ducdiep.bookmarket.models.Order

class ManageOrderAdapter(
    var context: Context,
    var list: List<Order>,
    var callbackClick: (Order) -> Unit,
    var callBackChangeStatus: (String, Int) -> Unit
) :
    RecyclerView.Adapter<ManageOrderAdapter.ManageOrderViewHolder>() {
    inner class ManageOrderViewHolder(val binding: ItemManageOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(order: Order) {
            binding.tvOrderId.text = "#${order.order_id}"
            binding.tvTimeOrder.text = order.created_at
            val spAdapter =
                SpinnerStatusOrderAdapter(context, listOf("Đang chờ", "Đã giao", "Đã nhận", "Đã hủy"))
            spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spStatusOrder.adapter = spAdapter
            binding.spStatusOrder.setSelection(order.status)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageOrderAdapter.ManageOrderViewHolder {
        val binding = ItemManageOrderBinding.inflate(LayoutInflater.from(context))
        val holder = ManageOrderViewHolder(binding)
        initListener(holder)
        return holder
    }

    private fun initListener(holder: ManageOrderAdapter.ManageOrderViewHolder) {
        holder.binding.rlBody.setOnClickListener {
            callbackClick.invoke(list[holder.layoutPosition])
        }
        holder.binding.spStatusOrder.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                callBackChangeStatus.invoke(list[holder.layoutPosition].order_id,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onBindViewHolder(holder: ManageOrderAdapter.ManageOrderViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<Order>){
        this.list = list.reversed()
        notifyDataSetChanged()
    }
}