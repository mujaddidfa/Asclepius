package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.repository.NewsRepository
import com.dicoding.asclepius.data.local.room.Databases
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = Databases.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}