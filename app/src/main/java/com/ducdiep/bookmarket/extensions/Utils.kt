package com.ducdiep.bookmarket.extensions

import android.util.Log
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun getTimeNow(): String {
    val date = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    return dateFormat.format(date)
}

fun createInvisiblePassword(length: Int): String {
    var result = ""
    repeat(length) {
        result += "*"
    }
    return result
}

fun getFormatMoney(long: Long):String{
    return NumberFormat.getNumberInstance(Locale("vi")).format(long) + " vnđ"
}