package com.ducdiep.bookmarket.ui.manage.statistical

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_ORDER
import com.ducdiep.bookmarket.extensions.createCalendarInstance
import com.ducdiep.bookmarket.models.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.LinkedHashMap

class StatisticalViewModel : ViewModel() {
    var dataOrder = FirebaseDatabase.getInstance().getReference(TABLE_ORDER)
    var listOrder = MutableLiveData<List<Order>>()
    var listDataChart = MutableLiveData<LinkedHashMap<Int, Long>>()

    init {
        getListOrder()
    }

    fun getListDataChart(month: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        var day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.d("abcc", "Day of mont: $day")
        val linkedHashMap = linkedMapOf<Int, Long>()
        for (i in 1..day) {
            linkedHashMap.set(i, 0)
        }
        Log.d("abcc", "link list: $linkedHashMap ")
        listOrder.value?.forEach { order ->
            var calendar = order.created_at.createCalendarInstance()
            if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                val dayOfMonth = calendar.get(Calendar.DATE)
                Log.d("abcc", "day of month: $dayOfMonth")
                val money = linkedHashMap.get(dayOfMonth)
                money?.let {
                    linkedHashMap.set(dayOfMonth, it + order.total_amount)
                }
            }
        }
        listDataChart.value = linkedHashMap
        Log.d("abcc", "link list: $linkedHashMap ")

    }

    fun getListOrder() {
        dataOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<Order>()
                snapshot.children.forEach {
                    val order = it.getValue(Order::class.java)
                    if (order != null) {
                        listTemp.add(order)
                    }
                }
                listOrder.value = listTemp
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}