package com.dino.connectnews.data.network

import com.dino.connectnews.data.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewService {
    @GET("{articles}")
    fun getNewsByCategory(
        @Path("articles") articles: String?,
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?,
        @Query("category") category: String?,
        @Query("page") page : Int
    ): Call<News?>?
}