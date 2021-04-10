package com.volboy.course_project.repository

import androidx.room.TypeConverter
import com.volboy.course_project.model.ReactionsJSON
import java.lang.StringBuilder

class MessageConverter {
    @TypeConverter
    fun fromReactions(listReactions: List<ReactionsJSON>): String {
        val stringBuilder: StringBuilder = StringBuilder("")
        listReactions.forEach { r ->
            stringBuilder.append(
                "?${r.emoji_code},${r.emoji_name},${r.reaction_type},${r.user_id},"
            )
        }
        return stringBuilder.toString()
    }
    @TypeConverter
    fun toReactions(stringReactions: String): List<ReactionsJSON> {
        val listReactions = mutableListOf<ReactionsJSON>()
        val stringList = stringReactions.split(",")
        val stringListGrouped = stringList.groupBy { "?" }
        stringListGrouped.forEach { (key, list) ->
            listReactions.add(ReactionsJSON(list[0], list[1], list[2], list[3].toInt()))
        }
        return listReactions
    }

    @TypeConverter
    fun fromArrayString(array: Array<String>): String {
        val stringBuilder: StringBuilder = StringBuilder("")
        array.forEach { str ->
            stringBuilder.append("?$str")
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toArrayString(string: String): Array<String> {
        val array = string.split("?")
        return array.toTypedArray()
    }
}