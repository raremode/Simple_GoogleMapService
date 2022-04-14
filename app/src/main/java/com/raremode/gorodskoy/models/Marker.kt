package com.raremode.gorodskoy.models

data class Marker(
    val id: Int? = null,
    val name: String? = null,
    val garbageType: String? = null,
    val coordinates: Coordinates
)