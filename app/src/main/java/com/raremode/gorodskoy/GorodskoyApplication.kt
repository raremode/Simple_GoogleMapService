package com.raremode.gorodskoy

import android.app.Application
import com.google.android.libraries.places.api.Places

class GorodskoyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, "AIzaSyDzUo-5esaP9nINEqU3qhghSzpcdje7V24")
    }
}