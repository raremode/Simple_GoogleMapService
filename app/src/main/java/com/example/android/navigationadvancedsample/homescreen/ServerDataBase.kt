package com.example.android.navigationadvancedsample.homescreen

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.StringBuilder

class ServerDataBase(context: Context?) {
private lateinit var fileJSONObject: JSONObject
private lateinit var jsonArray : JSONArray
    private val TAG = "ServerActivity"

fun loadJSON(){
    val file = File("file///:android_asset/", "markers.json")
    val fileReader = FileReader(file)
    val bufferedReader = BufferedReader(fileReader)
    val stringBuilder = StringBuilder()
    var line: String = bufferedReader.readLine()

    while (line != null) {
        stringBuilder.append(line).append("\n")
        line = bufferedReader.readLine()
    }
    bufferedReader.close()
// Этот ответ будет иметь формат Json String
// Этот ответ будет иметь формат Json String
    val response = stringBuilder.toString()

    fileJSONObject = JSONObject(response)

    jsonArray = fileJSONObject.getJSONArray("markers");

    for (i in 0 until jsonArray.length()){
        val obj=jsonArray.getJSONObject(i)
        var idPlace = obj.getString("id")
        var gType = obj.getString("garbageTypes")
        var latPlace = obj.getDouble("latitude")
        var lonPlace = obj.getDouble("longitude")

        Log.d(TAG, "id=$idPlace, garbageTypes=$gType, latitude=$latPlace, longitude=$lonPlace ;")
    }
}
}