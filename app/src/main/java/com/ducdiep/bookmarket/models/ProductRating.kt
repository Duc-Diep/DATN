package com.ducdiep.bookmarket.models

data class ProductRating(
    val book_id: String,
    val comment: Int,
    val created_at: String,
    val rating_id: String,
    val star: Int,
    val user_id: String
)