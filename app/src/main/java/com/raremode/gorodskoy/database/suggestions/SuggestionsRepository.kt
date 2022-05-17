package com.raremode.gorodskoy.database.suggestions

class SuggestionsRepository(private val suggestionsDao: SuggestionsDao) {

    suspend fun addSuggestionItem(suggestion: SuggestionModel) {
        suggestionsDao.addItem(suggestion = suggestion)
    }

    suspend fun getAllItems(): List<SuggestionModel> {
        return suggestionsDao.getAllItems()
    }

    suspend fun deleteAllSuggestions() {
        suggestionsDao.deleteAllSuggestions()
    }

}