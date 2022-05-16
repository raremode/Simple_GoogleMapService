package com.raremode.gorodskoy.database.suggestions

import androidx.room.*

@Dao
interface SuggestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(suggestion: SuggestionModel)

    @Query("SELECT * FROM suggestion_table")
    suspend fun getAllItems(): List<SuggestionModel>

    @Query("DELETE FROM suggestion_table")
    suspend fun deleteAllSuggestions()
}