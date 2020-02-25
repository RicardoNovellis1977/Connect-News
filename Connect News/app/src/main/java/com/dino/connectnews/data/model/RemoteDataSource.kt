package com.dino.connectnews.data.model

import android.content.Context
import com.dino.connectnews.data.network.RetrofitConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type

class RemoteDataSource {

    var APIKEY = "e6f7fdc7f1054eb491db5d5552c1970b"
    var COUNTRY = "br"
    var ARTICLES = "top-headlines"

    fun listNews(categoria: String?, page: Int): Observable<News> {
        return RetrofitConfig.buildRetrofit
            .getNewsByCategory(ARTICLES, COUNTRY, APIKEY, categoria, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLoadNews(context: Context): Observable<ArrayList<Article>>? {

        var article: ArrayList<Article>? = null
        val mPrefs = context.getSharedPreferences("article", 0)
        val gson = Gson()
        val json = mPrefs.getString("myJson", "")
        article = if (json!!.isEmpty()) {
            ArrayList<Article>()
        } else {
            val type: Type =
                object : TypeToken<List<Article?>?>() {}.type
            gson.fromJson<ArrayList<Article>>(json, type)
        }
        return Observable.fromArray(article)
    }
}