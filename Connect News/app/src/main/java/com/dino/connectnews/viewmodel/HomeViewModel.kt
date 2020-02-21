package com.dino.connectnews.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dino.connectnews.view.adapter.NewsAdapter
import com.dino.connectnews.data.network.RetrofitConfig
import com.dino.connectnews.data.network.NewService
import com.dino.connectnews.data.model.Article
import com.dino.connectnews.data.model.News
import com.dino.connectnews.view.fragment.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class HomeViewModel : ViewModel() {

    private var article: ArrayList<Article>? = ArrayList()
    var list : MutableLiveData<Article> = MutableLiveData()
    private var service: NewService = RetrofitConfig.buildRetrofit()!!.create(
        NewService::class.java)
    var PAGE = 1
    var APIKEY = "e6f7fdc7f1054eb491db5d5552c1970b"
    var COUNTRY = "br"
    var ARTICLES = "top-headlines"
    private var category: String? = null

    fun newInstance(categoria: String?): HomeFragment {
        val args = Bundle()
        args.putString("categoria", categoria)
        val fragment: HomeFragment =
            HomeFragment()
        fragment.setArguments(args)
        return fragment
    }

    fun getNews(adapterNews: NewsAdapter,
                fragment: HomeFragment,
                context: Context
    ){
        category = fragment.arguments?.getString("categoria")
        val call: Call<News?>? =
            service?.getNewsByCategory(ARTICLES, COUNTRY, APIKEY, category, PAGE)
        call?.enqueue(object : Callback<News?> {
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                if (response.body()?.articles != null){
                    article = response.body()?.articles
                    adapterNews.adicioarNews(article)
                    saveSharedPreferencesNews(context, article)
                    fragment.refreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
            }
        })
    }

    fun getNextPage (adapterNews: NewsAdapter,
                     fragment: HomeFragment,
                     context: Context,
                     recyclerView : RecyclerView){

        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        //adapterNews = NewsAdapter(article)

        val endHasBeenReached = lastVisible + 3 > totalItemCount

        if (totalItemCount > 0 && endHasBeenReached && PAGE < 3) {
            PAGE++
            getNews(adapterNews,fragment,context)
            adapterNews.update(article)

        }
    }

    fun getRefresh(adapterNews: NewsAdapter, refreshLayout: SwipeRefreshLayout,
                   fragment: HomeFragment,
                   context: Context){
        refreshLayout.setOnRefreshListener {
            PAGE = 1
            getNews(adapterNews,fragment,context)
            adapterNews.update(article)
        }

    }
    fun isOnline(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun saveSharedPreferencesNews(
        context: Context,
        news: ArrayList<Article>?
    ) {
        val mPrefs: SharedPreferences =
            context.getSharedPreferences("article", 0)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(news)
        prefsEditor.putString("myJson", json)
        prefsEditor.commit()
    }

    fun loadSharedPreferencesNews(context: Context): ArrayList<Article>? {

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
        return article
    }

}

