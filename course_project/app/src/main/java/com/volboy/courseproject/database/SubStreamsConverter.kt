package com.volboy.courseproject.database

import androidx.room.TypeConverter

class SubStreamsConverter {
    @TypeConverter
    fun fromArrayInt(array: ArrayList<Int>?): String {
        val stringBuilder: StringBuilder = StringBuilder("")
        array?.forEach { int ->
            stringBuilder.append("?$int")
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toArrayInt(string: String): ArrayList<Int> {
        val array = string.split("?")
        val intArray = arrayListOf<Int>()
        if (string.isNotEmpty()) {
            array.map { str ->
                intArray.add(str.toInt())
            }
        }
        return intArray
    }
}