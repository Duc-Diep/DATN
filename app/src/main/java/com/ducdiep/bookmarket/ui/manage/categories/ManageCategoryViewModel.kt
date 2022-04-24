package com.ducdiep.bookmarket.ui.manage.categories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_CATEGORIES
import com.ducdiep.bookmarket.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageCategoryViewModel:ViewModel() {
    var listCategories = MutableLiveData<ArrayList<Category>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_CATEGORIES)
    var isLoading = MutableLiveData<Boolean>()

    init {
        getAllCategories()
        getLastKey()
    }

    private fun getAllCategories() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listTemp = arrayListOf<Category>()
                snapshot.children.forEach {
                    val category = it.getValue(Category::class.java)
                    Log.d("userr", "onDataChange: $category")
                    if (category != null) {
                        listTemp.add(category)
                    }
                }
                Log.d("userr", "onDataChange: ${listCategories.value}")
                listCategories.value = listTemp
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

    fun addCategory(category: Category){
        var hm = HashMap<String,Any>()
        hm["category_id"] = ""
        hm["created_at"] = category.created_at
        hm["updated_at"] = category.updated_at
        hm["name"] = category.name
        data.push().setValue(hm)
    }

    fun getLastKey(){
        data.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.last().key.toString()
                val newId = hashMapOf<String,Any>("category_id" to lastKey)
                data.child(lastKey).updateChildren(newId)
                Log.d("key", "onDataChange: $lastKey")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun removeById(id:String){
        data.child(id).removeValue()
    }
}