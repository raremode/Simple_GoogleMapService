package com.raremode.gorodskoy.database.suggestions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestion_table")
data class SuggestionModel(
    @PrimaryKey val suggestion: String
)