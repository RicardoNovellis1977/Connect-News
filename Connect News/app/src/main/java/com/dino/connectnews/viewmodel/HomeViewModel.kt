package com.dino.connectnews.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dino.connectnews.data.NewsRepository
import com.dino.connectnews.data.NewsRepositoryImpl
import com.dino.connectnews.data.model.Article
import com.dino.connectnews.data.model.News

class HomeViewModel(private val repository: NewsRepository = NewsRepositoryImpl()) : ViewModel() {

    fun getNews(categoria: String,page : Int): LiveData<News> {
        return repository.getAllNews(categoria,page)
    }


    fun isOnline(context: Context): Boolean {
        return repository.online(context)
    }

    fun saveSharedPreferencesNews(context: Context, news: ArrayList<Article>?) {
        return repository.saveNews(context, news)
    }

    fun loadSharedPreferencesNews(context: Context): LiveData<ArrayList<Article>>? {
       return repository.loadNews(context)
    }
}

