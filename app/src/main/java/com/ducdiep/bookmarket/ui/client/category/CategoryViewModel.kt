package com.ducdiep.bookmarket.ui.client.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_CATEGORIES
import com.ducdiep.bookmarket.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryViewModel:ViewModel() {
    var listCategories = MutableLiveData<ArrayList<Category>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_CATEGORIES)
    var isLoading = MutableLiveData<Boolean>()

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listTemp = arrayListOf<Category>()
                snapshot.children.forEach {
                    val category = it.getValue(Category::class.java)
                    if (category != null) {
                        listTemp.add(category)
                    }
                }
                listCategories.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }
}