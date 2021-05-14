package com.example.appcentnewsapp.service

import android.content.Context
import androidx.room.*
import com.example.appcentnewsapp.model.ArticleModel

@Database(entities = arrayOf(ArticleModel::class),version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun articleDAO() : ArticleDAO

    companion object{
        @Volatile private var instance : ArticleDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDatabase(context).also{
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
        ArticleDatabase::class.java,"articledatabase").build()
        
    }
}