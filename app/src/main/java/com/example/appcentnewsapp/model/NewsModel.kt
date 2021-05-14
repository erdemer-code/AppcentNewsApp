package com.example.appcentnewsapp.model

data class NewsModel(
    val articles: MutableList<ArticleModel>,
    val status: String,
    val totalResults: Int
)