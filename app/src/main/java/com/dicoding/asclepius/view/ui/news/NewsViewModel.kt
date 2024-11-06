package com.dicoding.asclepius.view.ui.news

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repository.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()
}