package com.ducdiep.bookmarket.models

data class Book(
    val author: String,
    val book_id: String,
    val category_id: String,
    val created_at: String,
    val current_number: Int,
    val description: String,
    val image: String,
    val name: String,
    val num_of_page: Int,
    val price: Int,
    val publisher: String,
    val updated_at: String
)