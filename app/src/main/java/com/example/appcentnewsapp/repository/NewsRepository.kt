package com.example.appcentnewsapp.repository

import com.example.appcentnewsapp.model.ArticleModel
import com.example.appcentnewsapp.service.ArticleDatabase
import com.example.appcentnewsapp.service.NewsAPIService

class NewsRepository(val database: ArticleDatabase) {
    suspend fun getAllArticles(q:String,pageNumber:Int)= NewsAPIService.api.searchNews(q,pageNumber)

    suspend fun insert(article:ArticleModel) = database.articleDAO().insert(article)

    fun getFavoriteNews() = database.articleDAO().getAllFavoriteArticles()

    suspend fun deleteArticle(article: ArticleModel) = database.articleDAO().deleteNews(article)

    suspend fun  deleteAllArticle() = database.articleDAO().deleteAllNews()

}