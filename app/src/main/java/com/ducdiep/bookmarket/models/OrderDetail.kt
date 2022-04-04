package com.ducdiep.bookmarket.models

data class OrderDetail(
    val book_id: String,
    val created_at: String,
    val order_detail_id: String,
    val order_id: String,
    val quantity: Int,
    val updated_at: String
)