package com.example.android.navigationadvancedsample.homescreen

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context?) :



    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $DB_MARKERS($TYPEFILTER integer, $LATITUDE real,$LONGITUDE real )")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists $DB_MARKERS")
        onCreate(db)
    }


    companion object {
        const val DB_VERSION = 7
        const val DB_NAME = "db"
        const val DB_PATH = " "
        const val DB_MARKERS = "markers"
        const val TYPEFILTER = "type"
        const val LATITUDE = "x" //широта-это снизу вверх
        const val LONGITUDE = "y" //долгота это слева направо как бы
    }
}