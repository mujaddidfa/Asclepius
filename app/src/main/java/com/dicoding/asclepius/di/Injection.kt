package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.data.local.room.NewsDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}