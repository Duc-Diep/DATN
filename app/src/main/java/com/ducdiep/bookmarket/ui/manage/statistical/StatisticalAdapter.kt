package com.ducdiep.bookmarket.ui.manage.statistical

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookmarket.databinding.ItemStatisticalBinding
import com.ducdiep.bookmarket.extensions.getFormatMoney
import java.util.LinkedHashMap

class StatisticalAdapter(var context: Context,var listData: LinkedHashMap<Int, Long>):RecyclerView.Adapter<StatisticalAdapter.StatisViewModel>() {
    class StatisViewModel(val binding:ItemStatisticalBinding):RecyclerView.ViewHolder(binding.root) {
        fun onBind(day:Int,money:Long){
            if (day>0){
                binding.tvDay.text = "Ngày $day"
                binding.tvMoney.text = getFormatMoney(money) + "vnđ"
            }else{
                binding.tvDay.text = ""
                binding.tvMoney.text = ""
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisViewModel {
        val binding = ItemStatisticalBinding.inflate(LayoutInflater.from(context))
        return StatisViewModel(binding)
    }

    override fun onBindViewHolder(holder: StatisViewModel, position: Int) {
        holder.onBind(position, listData[position]?:0)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    fun setData(listData: LinkedHashMap<Int, Long>){
        this.listData = listData
        notifyDataSetChanged()
    }
}