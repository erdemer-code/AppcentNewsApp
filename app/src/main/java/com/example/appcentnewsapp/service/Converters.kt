package com.example.appcentnewsapp.service

import androidx.room.TypeConverter
import com.example.appcentnewsapp.model.SourceModel

class Converters {

    @TypeConverter
    fun fromSource(source: SourceModel):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):SourceModel{
        return SourceModel(name,name)
    }

}