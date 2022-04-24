package com.ducdiep.bookmarket.ui.manage.books

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageBookViewModel : ViewModel() {
    var listBooks = MutableLiveData<ArrayList<Book>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var isLoading = MutableLiveData<Boolean>()
    var bookDetails = MutableLiveData<Book>()

    init {
        getAllBooks()
        getLastKey()
    }

    private fun getAllBooks() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listTemp = arrayListOf<Book>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    Log.d("userr", "onDataChange: $book")
                    if (book != null) {
                        listTemp.add(book)
                    }
                }
                Log.d("userr", "onDataChange: ${listBooks.value}")
                listBooks.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun updateField(id: String, hm: HashMap<String, Any>) {
        data.child(id).updateChildren(hm)
    }

    fun getBookById(id: String) {
        data.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookDetails.value = snapshot.getValue(Book::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                bookDetails.value = null
            }
        })
    }

    fun removeById(id:String){
        data.child(id).removeValue()
    }

    fun addBook(book: Book){
        val hm = HashMap<String,Any>()
        hm["author"] = book.author
        hm["book_id"] = book.book_id
        hm["category_id"] = book.category_id
        hm["created_at"] = book.created_at
        hm["current_number"] = book.current_number
        hm["description"] = book.description
        hm["image"] = book.image
        hm["name"] = book.name
        hm["num_of_page"] = book.num_of_page
        hm["price"] = book.price
        hm["publisher"] = book.publisher
        hm["updated_at"] = book.updated_at
        data.push().setValue(hm)
    }

    fun getLastKey(){
        data.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.last().key.toString()
                val newId = hashMapOf<String,Any>("book_id" to lastKey)
                data.child(lastKey).updateChildren(newId)
                Log.d("key", "onDataChange: $lastKey")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}