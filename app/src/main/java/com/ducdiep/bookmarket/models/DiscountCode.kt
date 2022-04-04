package com.ducdiep.bookmarket.models

data class DiscountCode(
    val code: String,
    val created_at: String,
    val discount_id: String,
    val is_expire: Boolean,
    val remaining: Int
)