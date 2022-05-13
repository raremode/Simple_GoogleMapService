package com.raremode.gorodskoy.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.raremode.gorodskoy.AppConfig.APP_TAG
import com.raremode.gorodskoy.AppConfig.Assets.JSON_ASSETS_FILE_NAME
import com.raremode.gorodskoy.database.MarkerDao
import com.raremode.gorodskoy.database.MarkerDatabase
import com.raremode.gorodskoy.models.MarkerLocation
import com.raremode.gorodskoy.models.RootModel

class JsonAssetsManager(private val context: Context) {

    private var rootModel: RootModel? = null

    fun parseJsonFromAssets(callback: (List<MarkerLocation>) -> Unit) {
        runCatching<String?> {
            context.assets.open(JSON_ASSETS_FILE_NAME).bufferedReader().use { it.readText() }
        }.onSuccess { jsonString ->
            rootModel = Gson().fromJson(jsonString, RootModel::class.java)
            rootModel?.markers?.let { callback(it) }
        }.onFailure {
            Log.d(APP_TAG, it.message.toString())
        }
    }

    fun getMarkers(): List<MarkerLocation>? = rootModel?.markers
}