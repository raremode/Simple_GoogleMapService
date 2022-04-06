package com.example.android.navigationadvancedsample.listscreen

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase


class DataBase(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + DB_TYPE + "(" + WIDTH + " text," + LONGITUDE + " text " + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists " + DB_TYPE)
        onCreate(db)
    }

    companion object {
        const val DB_VERSION = 6
        const val DB_NAME = "db"
        const val DB_TYPE = "markers"
        const val WIDTH = "x" //широта-это снизу вверх
        const val LONGITUDE = "y" //долгота это слева направо как бы
    }
}