package com.example.appcentnewsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcentnewsapp.model.ArticleModel
import com.example.appcentnewsapp.model.NewsModel
import com.example.appcentnewsapp.repository.NewsRepository
import com.example.appcentnewsapp.service.NewsAPI
import com.example.appcentnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    val articles:MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    var articlesPage = 1
    var newsResponse: NewsModel? = null


    init {
        getAllArticles("tesla") // default value
    }

    fun getAllArticles(q:String) = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        val response = newsRepository.getAllArticles(q,articlesPage)
        articles.postValue(handleArticlesResponse(response))
    }

    fun getAllNewArticles(q:String) = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        val response = newsRepository.getAllArticles(q,articlesPage)
        articles.postValue(handleNewArticlesResponse(response))
    }

    private fun handleArticlesResponse(response: Response<NewsModel>) : Resource<NewsModel>{
        if (response.isSuccessful){
            response.body()?.let {
                articlesPage++
                if(newsResponse == null){
                    newsResponse = it
                } else {
                    val oldArticles = newsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(newsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleNewArticlesResponse(response: Response<NewsModel>):Resource<NewsModel>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun makeFavoriteArticle(article:ArticleModel) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getFavoriteNews() = newsRepository.getFavoriteNews()

    fun deleteArticle(article: ArticleModel) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    fun deleteAllArticle() = viewModelScope.launch {
        newsRepository.deleteAllArticle()
    }
}


