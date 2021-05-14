package com.example.appcentnewsapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appcentnewsapp.model.ArticleModel

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article:ArticleModel) : Long

    @Query("SELECT * FROM articles")
     fun getAllArticles():LiveData<ArticleModel>

     @Query("SELECT *FROM articles")
     fun getAllFavoriteArticles():LiveData<List<ArticleModel>>

     @Query("DELETE  FROM articles")
     suspend fun deleteAllNews()


    @Delete
    suspend fun deleteNews(article: ArticleModel)

}