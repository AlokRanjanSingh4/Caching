package com.example.cachecheck.data.remote.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.cachecheck.data.remote.dto.PostResponse
import kotlin.system.measureTimeMillis

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_cache"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "my_table"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_KEY TEXT," +
                "$COLUMN_VALUE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addData(key: String, value: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_KEY, key)
            put(COLUMN_VALUE, value)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun getAllData(): List<Pair<String, String>> {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_KEY, COLUMN_VALUE)
        val cursor = db.query(
            TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val dataList = mutableListOf<Pair<String, String>>()
        with(cursor) {
            while (moveToNext()) {
                val key = getString(getColumnIndexOrThrow(COLUMN_KEY))
                val value = getString(getColumnIndexOrThrow(COLUMN_VALUE))
                dataList.add(Pair(key, value))
            }
            close()
        }
        db.close()
        return dataList
    }

    fun getDataForKey(key: String): String? {
        val db = readableDatabase
        val projection = arrayOf(COLUMN_VALUE)
        val selection = "$COLUMN_KEY = ?"
        val selectionArgs = arrayOf(key)
        val cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var value: String? = null
        with(cursor) {
            if (moveToFirst()) {
                value = getString(getColumnIndexOrThrow(COLUMN_VALUE))
            }
            close()
        }
        db.close()
        return value
    }

    fun measureTimeToAddAndRetrieveData(context: Context) {
        val dbHelper = DatabaseHelper(context)

        val addTime = measureTimeMillis {
            for (i in 1..1000) {
                dbHelper.addData("key$i", "value$i")
            }
        }
        Log.d("Testing", "DB Time taken to add data: $addTime milliseconds")

        val retrieveTime = measureTimeMillis {
            dbHelper.getAllData()
        }

        Log.d("Testing", "DB Time taken to retrieve data: $retrieveTime milliseconds")

        val getKeyTime = measureTimeMillis {
            for (i in 1..1000) {
                dbHelper.getDataForKey("key$i")
            }
        }
        Log.d("Testing", "DB Time taken to getDataForKey: $getKeyTime milliseconds")

    }

    fun measureTimeToAddAndRetrieveApiData(context: Context, res: List<PostResponse>) {
        val dbHelper = DatabaseHelper(context)

        val addTime = measureTimeMillis {
            for (i in 1..1000) {
                dbHelper.addData("key$i", "value${res.toString()}")
            }
        }
        Log.d("Testing", "DB Time taken to add data: $addTime milliseconds")

        val retrieveTime = measureTimeMillis {
            dbHelper.getAllData()
        }

        Log.d("Testing", "DB Time taken to retrieve data: $retrieveTime milliseconds")

        val getKeyTime = measureTimeMillis {
            for (i in 1..1000) {
                dbHelper.getDataForKey("key$i")
            }
        }
        Log.d("Testing", "DB Time taken to getDataForKey: $getKeyTime milliseconds")

    }
}

//D  DB Time taken to add data: 1916 milliseconds
//D  DB Time taken to retrieve data: 150 milliseconds
//D  DB Time taken to getDataForKey: 335 milliseconds