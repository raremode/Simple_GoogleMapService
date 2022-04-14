package com.raremode.gorodskoy.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList


class DataBaseWork(context: Context?) {

    private val marks: DataBase
    private val cv: ContentValues
    private val db: SQLiteDatabase
    private var index = 0
    private var index2 = 0
    private var indexType = 0
    private var numbertype: Int = 0
    var cursor: Cursor? = null
    var cursor2: Cursor? = null

    fun loadDb() {

    }

    fun insert(
        type: Int?,
        latitude: Double?,
        longitude: Double?
    ) { //функция заполнения БД точек сбора мусора
        cv.put(DataBase.TYPEFILTER, type)
        cv.put(DataBase.LATITUDE, latitude)
        cv.put(DataBase.LONGITUDE, longitude)
        db.insert(DataBase.DB_MARKERS, null, cv)
    }

    /*  fun rec_x(type: Int?): ArrayList<Double> { //получение переменных широты
          val alist = ArrayList<Double>()
          cursor = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
          while (cursor!!.moveToNext()) {
              indexType = cursor2!!.getColumnIndex(DataBase.TYPEFILTER) //new
              numbertype = cursor2!!.getInt(indexType) //new
              index = cursor!!.getColumnIndex(DataBase.LATITUDE)
              if(numbertype != type){}
              else {
                  alist.add(cursor!!.getDouble(index))
              }
          }
          return alist
      }

      fun rec_y(type: Int?): ArrayList<Double> { //получение переменных долготы
          val alist2 = ArrayList<Double>()
          cursor2 = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
          while (cursor2!!.moveToNext()) {
              indexType = cursor2!!.getColumnIndex(DataBase.TYPEFILTER) //new
             numbertype = cursor2!!.getInt(indexType) //new
                  index2 = cursor2!!.getColumnIndex(DataBase.LONGITUDE)
              if(numbertype != type) {

              }
              else{
                  alist2.add(cursor2!!.getDouble(index2))
              }
          }
          return alist2
      } */ //старые функции, на всякий случай оставил их

    fun getGarbageLocations(type: Int?): ArrayList<LatLng> { //получение переменных LatLng!
        val alist = ArrayList<LatLng>()
        cursor = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
        cursor2 = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
        while (cursor!!.moveToNext() && cursor2!!.moveToNext()) {
            indexType = cursor2!!.getColumnIndex(DataBase.TYPEFILTER) //new
            numbertype = cursor2!!.getInt(indexType) //new
            index = cursor!!.getColumnIndex(DataBase.LATITUDE)
            index2 = cursor!!.getColumnIndex(DataBase.LONGITUDE)
            if (type != 0) {
                if (numbertype != type) {
                } else {
                    var indexLatitude = cursor!!.getDouble(index)
                    var indexLongitude = cursor2!!.getDouble(index2)
                    var latLng: LatLng = LatLng(indexLatitude, indexLongitude)
                    alist.add(latLng)
                }
            } else {
                var indexAllLatitude = cursor!!.getDouble(index)
                var indexAllLongitude = cursor2!!.getDouble(index2)
                var latLng: LatLng = LatLng(indexAllLatitude, indexAllLongitude)
                alist.add(latLng)
            }
        }
        return alist
    }

    fun getGarbageTypes(): ArrayList<Int> {
        val alist = ArrayList<Int>()
        cursor = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
        while (cursor!!.moveToNext()) {
            indexType = cursor!!.getColumnIndex(DataBase.TYPEFILTER) //new
            numbertype = cursor!!.getInt(indexType) //new
            alist.add(numbertype)
        }
        return alist
    }

    fun get_value(): Int { //получение размера БД точек сбора мусора
        cursor = db.query(DataBase.DB_MARKERS, null, null, null, null, null, null)
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