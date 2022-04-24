package com.ducdiep.bookmarket.models

import java.io.Serializable

data class Book(
    val author: String = "",
    val book_id: String = "",
    val category_id: String = "",
    val created_at: String = "",
    val current_number: Int = 0,
    val description: String = "",
    val image: String = "",
    val name: String = "",
    val num_of_page: Int = 0,
    val price: Long = 0,
    val publisher: String = "",
    val updated_at: String = ""
) : Serializable