package com.raremode.gorodskoy.ui.models

import com.raremode.gorodskoy.models.GarbageTypes

data class FilterButtonModel(
    val text: String,
    val type: GarbageTypes,
    var isSelected: Boolean
)