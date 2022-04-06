package com.example.android.navigationadvancedsample.listscreen

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import java.util.ArrayList


class DataBase_work(context: Context?) {
    private val marks: DataBase
    private val cv: ContentValues
    private val db: SQLiteDatabase
    private var index = 0
    private var index2 = 0
    var cursor: Cursor? = null
    var cursor2: Cursor? = null
    fun insert(width: String?, longitude: String?) { //функция заполнения БД точек сбора мусора
        cv.put(DataBase.WIDTH, width)
        cv.put(DataBase.LONGITUDE, longitude)
        db.insert(DataBase.DB_TYPE, null, cv)
    }

    fun rec_x(): ArrayList<String> { //получение переменных широты
        val alist = ArrayList<String>()
        cursor = db.query(DataBase.DB_TYPE, null, null, null, null, null, null)
        while (cursor!!.moveToNext()) {
            index = cursor!!.getColumnIndex(DataBase.WIDTH)
            alist.add(cursor!!.getString(index))
        }
        return alist
    }

    fun rec_y(): ArrayList<String> { //получение переменных долготы
        val alist2 = ArrayList<String>()
        cursor2 = db.query(DataBase.DB_TYPE, null, null, null, null, null, null)
        while (cursor2!!.moveToNext()) {
            index2 = cursor2!!.getColumnIndex(DataBase.LONGITUDE)
            alist2.add(cursor2!!.getString(index2))
        }
        return alist2
    }

    fun get_value(): Int { //получение размера БД точек сбора мусора
        cursor = db.query(DataBase.DB_TYPE, null, null, null, null, null, null)
        while (cursor!!.moveToNext()) {
            index = index + 1
        }
        Log.w("value", " $index")
        return index
    }

    fun drawSimpleBitmap(): Bitmap { //изменение цвета значка точки сбора мусора
        val picSize = 25
        val bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // отрисовка плейсмарка
        val paint = Paint()
        paint.color = Color.rgb(255, 69, 0)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(
            (picSize / 2).toFloat(),
            (picSize / 2).toFloat(),
            (picSize / 2).toFloat(),
            paint
        )
        return bitmap
    }

    fun drawSimpleBitmap2(): Bitmap { //точка пользователя
        val picSize = 28
        val bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // отрисовка плейсмарка
        val paint = Paint()
        paint.color = Color.rgb(127, 255, 212)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(
            (picSize / 2).toFloat(),
            (picSize / 2).toFloat(),
            (picSize / 2).toFloat(),
            paint
        )
        return bitmap
    }

    init {
        marks = DataBase(context)
        db = marks.writableDatabase
        cv = ContentValues()
    }
}