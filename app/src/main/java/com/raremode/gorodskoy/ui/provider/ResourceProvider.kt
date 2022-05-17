package com.raremode.gorodskoy.ui.provider

import android.app.Application

class ResourceProvider(private val application: Application) {

    fun getString(id: Int) = application.resources.getString(id)
}