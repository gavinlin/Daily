package com.gavincode.bujo.data.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 10/3/18.
 */

class DailyBulletConverters {

    @TypeConverter
    fun fromString(value: String): java.util.List<String> {
        val gson = Gson()
        return gson.fromJson(value, object: TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromStringList(value: java.util.List<String>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate): Long {
        return value.toEpochDay()
    }

    @TypeConverter
    fun fromLong(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

}