package com.example.appcentnewsapp.service

import com.example.appcentnewsapp.model.NewsModel
import com.example.appcentnewsapp.util.ConstantParams.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        q:String,
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ):Response<NewsModel>


}