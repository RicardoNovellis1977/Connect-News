package com.dino.connectnews.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.dino.connectnews.data.model.Article
import com.dino.connectnews.data.model.News

interface NewsRepository {

    fun getAllNews ( category : String,page : Int): LiveData<News>

    fun online(context: Context): Boolean

    fun saveNews(context: Context, news: ArrayList<Article>?)

    fun loadNews(context: Context): LiveData<ArrayList<Article>>?

}