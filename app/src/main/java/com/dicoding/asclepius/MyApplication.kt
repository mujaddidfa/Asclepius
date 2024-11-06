package com.dicoding.asclepius

import android.app.Application
import android.util.Log
import com.dicoding.asclepius.data.repository.NewsRepository
import com.dicoding.asclepius.data.local.room.Databases
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val newsDao = Databases.getInstance(this).newsDao()
        val apiService = ApiConfig.getApiService()
        val newsRepository = NewsRepository.getInstance(apiService, newsDao)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newsLiveData = newsRepository.getHeadlineNews()
                withContext(Dispatchers.Main) {
                    newsLiveData.observeForever {
                        Log.d("MyApplication", "getHeadlineNews result: $it")
                    }
                }
            } catch (e: Exception) {
                Log.e("MyApplication", "Error calling getHeadlineNews", e)
            }
        }
    }

}