package com.example.appcentnewsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appcentnewsapp.repository.NewsRepository

class ViewModelFactory(val newsRepository: NewsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository = newsRepository) as T
    }

}