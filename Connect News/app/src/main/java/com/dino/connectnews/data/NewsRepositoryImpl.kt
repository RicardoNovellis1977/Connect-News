package com.dino.connectnews.data

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dino.connectnews.data.model.Article
import com.dino.connectnews.data.model.News
import com.dino.connectnews.data.model.RemoteDataSource
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NewsRepositoryImpl : NewsRepository {

    val remoteDataSource = RemoteDataSource()
    val compositeDisposable = CompositeDisposable()

    override fun getAllNews(category: String, page: Int): LiveData<News> {

        val data = MutableLiveData<News>()

        val disposable = remoteDataSource.listNews(category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<News>() {
                override fun onComplete() {
                    println("Complete")
                }

                override fun onNext(dat: News) {
                    data.postValue(dat)
                }

                override fun onError(e: Throwable) {
                    data.postValue(null)
                }

            })
        compositeDisposable.add(disposable)

        return data
    }

    override fun loadNews(context: Context): LiveData<ArrayList<Article>>? {

        val data = MutableLiveData<ArrayList<Article>>()

        val disposable = remoteDataSource.getLoadNews(context)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ArrayList<Article>>() {
                override fun onComplete() {

                }
                override fun onNext(t: ArrayList<Article>) {
                    data.postValue(t)
                }
                override fun onError(e: Throwable) {
                    data.postValue(null)
                }
            })
        compositeDisposable.add(disposable!!)

        return data
    }

    override fun online(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    override fun saveNews(
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
}