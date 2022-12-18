package com.streamwide.haythemmejerbi.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    companion object{
        @JvmStatic
        @TypeConverter
        fun fromString(value: String): List<String> {
            val list = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(value, list)
        }
        @TypeConverter
        @JvmStatic
        fun fromStringList(list: List<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}
