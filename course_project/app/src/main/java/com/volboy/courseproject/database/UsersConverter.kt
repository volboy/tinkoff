package com.volboy.courseproject.database

import androidx.room.TypeConverter
import com.volboy.courseproject.model.IdData
import com.volboy.courseproject.model.ProfileData

class UsersConverter {
    @TypeConverter
    fun fromProfileData(list: List<IdData>): String {
        val stringBuilder: StringBuilder = StringBuilder("")
        list.forEach { idData ->
            idData.id.forEach { pD ->
                stringBuilder.append("?${pD.value},${pD.renderedValue},")
            }
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toProfileData(string: String): List<IdData> {
        val listIdData = mutableListOf<IdData>()
        val listString = string.split(",")
        val listStringGrouped = listString.groupBy { "?" }
        listStringGrouped.forEach { (_, list) ->
            listIdData.add(IdData(listOf(ProfileData(list[0], list[1]))))
        }
        return listIdData
    }
}