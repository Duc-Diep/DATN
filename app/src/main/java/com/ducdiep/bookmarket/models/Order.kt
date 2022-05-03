package com.ducdiep.bookmarket.models

data class Order(
    val created_at: String = "",
    val discount: Long = 0,
    val order_id: String = "",
    val total_amount: Long = 0,
    val updated_at: String = "",
    val user_id: String = "",
    val status:Int = 0
)