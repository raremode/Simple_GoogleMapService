package com.raremode.gorodskoy.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.raremode.gorodskoy.AppConfig.APP_TAG
import com.raremode.gorodskoy.AppConfig.Assets.JSON_ASSETS_FILE_NAME
import com.raremode.gorodskoy.models.Marker
import com.raremode.gorodskoy.models.RootModel

class JsonAssetsManager(private val context: Context?) {

    private var rootModel: RootModel? = null

    fun parseJsonFromAssets() {
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