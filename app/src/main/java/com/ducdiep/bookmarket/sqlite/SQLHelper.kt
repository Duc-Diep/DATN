package com.ducdiep.bookmarket.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val DB_NAME = "BookStore.db"
const val DB_VERSION = 1
const val DB_TABLE_CART = "Cart"

class SQLHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    lateinit var sqLiteDatabase: SQLiteDatabase
    lateinit var contentValues: ContentValues

    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateTableCart = "CREATE TABLE " + DB_TABLE_CART + "(" +
                "id Text PRIMARY KEY," +
                "number INTEGER)"
        db?.execSQL(queryCreateTableCart)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $DB_NAME")
            onCreate(db)
        }
    }

    fun insertOrUpdateBookToCart(id: String) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        if (getNumberBookInCart(id) == 0) {
            contentValues.put("id", id)
            contentValues.put("number", 1)
            sqLiteDatabase.insert(DB_TABLE_CART, null, contentValues)
        } else {
            contentValues.put("number", getNumberBookInCart(id) + 1)
            sqLiteDatabase.update(DB_TABLE_CART, contentValues, "id=?", arrayOf(id))
        }

    }

    fun minusBookInCart(id: String) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        if (getNumberBookInCart(id) == 1) {
            contentValues.put("number", 1)
        } else {
            contentValues.put("number", getNumberBookInCart(id) - 1)
        }
        sqLiteDatabase.update(DB_TABLE_CART, contentValues, "id=?", arrayOf(id))
    }

    fun getAllBookId(): HashMap<String, Int> {
        val list: HashMap<String, Int> = hashMapOf()
        sqLiteDatabase = readableDatabase
        val cursor =
            sqLiteDatabase.query(DB_TABLE_CART, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val number = cursor.getInt(cursor.getColumnIndex("number"))
            list.put(id, number)
        }
        return list
    }

    fun getNumberBookInCart(id: String): Int {
        sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.rawQuery(
            "SELECT * FROM $DB_TABLE_CART WHERE id=?",
            arrayOf(id)
        )
        Log.d("abc", "getNumberBookInCart: ${cursor.count}, ${cursor.columnNames[0]}, ${cursor.columnNames[1]}")

        if (cursor.count == 0) {
            return 0
        } else {
            cursor.moveToFirst()
            return cursor.getInt(cursor.getColumnIndex("number"))
        }
    }

    fun deleteAllBook() {
        sqLiteDatabase = writableDatabase
        sqLiteDatabase.delete(DB_TABLE_CART, null, null)
    }

    fun deleteBook(id: String) {
        sqLiteDatabase = writableDatabase
        sqLiteDatabase.delete(DB_TABLE_CART, "id=?", arrayOf(id))
    }
}