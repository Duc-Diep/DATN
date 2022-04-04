package com.ducdiep.bookmarket.models

data class Order(
    val created_at: Int,
    val discount: Int,
    val order_id: String,
    val total_amount: Int,
    val updated_at: String,
    val user_id: String
)