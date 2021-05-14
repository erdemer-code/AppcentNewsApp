package com.example.appcentnewsapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.repository.NewsRepository
import com.example.appcentnewsapp.service.ArticleDatabase
import com.example.appcentnewsapp.viewmodel.NewsViewModel
import com.example.appcentnewsapp.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel : NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = ViewModelFactory(newsRepository = repository )
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewsViewModel::class.java)
        bottom_nav_view.setupWithNavController(newsNavHostFragment.findNavController())


    }






}