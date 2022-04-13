package com.example.android.navigationadvancedsample.homescreen

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.example.android.navigationadvancedsample.AppConfig.APP_TAG
import com.example.android.navigationadvancedsample.models.Marker
import com.example.android.navigationadvancedsample.models.RootModel
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.StringBuilder

class ServerDataBase(private val context: Context?) {
    private lateinit var fileJSONObject: JSONObject
    private lateinit var jsonArray: JSONArray
    private var rootModel: RootModel? = null

    companion object {
        const val JSON_ASSETS_FILE_NAME = "markers.json"
    }

    fun loadJSON() {
        runCatching<String?> {
            context?.assets?.open(JSON_ASSETS_FILE_NAME)?.bufferedReader().use { it?.readText() }
        }.onSuccess { jsonString ->
            rootModel = Gson().fromJson(jsonString, RootModel::class.java)
        }.onFailure {
            Log.d(APP_TAG, it.message.toString())
        }
    }

    fun getMarkers(): List<Marker>? = rootModel?.markers
}