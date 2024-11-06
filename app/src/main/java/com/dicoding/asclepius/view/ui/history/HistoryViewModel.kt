package com.dicoding.asclepius.view.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val history: LiveData<List<HistoryEntity>> = repository.getAllHistory()

    suspend fun clearHistory() {
        repository.clearHistory()
    }
}