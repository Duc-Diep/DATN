package com.ducdiep.bookmarket.ui.client.home.bookdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.base.TABLE_PRODUCT_RATING
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.Comment
import com.ducdiep.bookmarket.models.ProductRating
import com.ducdiep.bookmarket.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookDetailViewModel : ViewModel() {

    var listRecommendBooks = MutableLiveData<ArrayList<Book>>()
    var listUser = MutableLiveData<ArrayList<User>>()
    var listRating = MutableLiveData<ArrayList<ProductRating>>()
    var listComment = MutableLiveData<ArrayList<Comment>>()

    var dataBook = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var dataUser = FirebaseDatabase.getInstance().getReference(TABLE_USERS)
    var dataRating = FirebaseDatabase.getInstance().getReference(TABLE_PRODUCT_RATING)

    init {
        getListUser()
        getLastKey()
    }

    fun getAllBooksByCategoryId(id: String, bid: String) {
        dataBook.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<Book>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null && book.category_id == id && book.book_id != bid) {
                        listTemp.add(book)
                    }
                }
                listRecommendBooks.value = listTemp
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getListRating(bid: String) {
        dataRating.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<ProductRating>()
                snapshot.children.forEach {
                    val rating = it.getValue(ProductRating::class.java)
                    if (rating != null && rating.book_id == bid) {
                        listTemp.add(rating)
                    }
                }
                listRating.value = listTemp
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getListUser() {
        dataUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<User>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        listTemp.add(user)
                    }
                }
                listUser.value = listTemp
                Log.d("abcc", "onDataChange: ${listUser.value}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getListComment() {
        var listTemp = arrayListOf<Comment>()
        listRating.value?.forEach { rating ->
            listUser.value?.forEach { user ->
                if (rating.user_id == user.user_id) {
                    listTemp.add(Comment(user, rating))
                }
            }
        }
        listComment.value = listTemp
    }

    fun addComment(productRating: ProductRating){
        var hm = HashMap<String,Any>()
        hm["book_id"] = productRating.book_id
        hm["created_at"] = productRating.created_at
        hm["updated_at"] = productRating.updated_at
        hm["comment"] = productRating.comment
        hm["rating_id"] = productRating.rating_id
        hm["star"] = productRating.star
        hm["user_id"] = productRating.user_id
        dataRating.push().setValue(hm)
    }

    fun getLastKey(){
        dataRating.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.last().key.toString()
                val newId = hashMapOf<String,Any>("rating_id" to lastKey)
                dataRating.child(lastKey).updateChildren(newId)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}