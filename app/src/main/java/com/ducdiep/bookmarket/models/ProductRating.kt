package com.ducdiep.bookmarket.models

data class ProductRating(
    val book_id: String = "",
    val comment: String = "",
    val created_at: String = "",
    val rating_id: String = "",
    val star: Float = 0F,
    val updated_at: String = "",
    val user_id: String = ""
)