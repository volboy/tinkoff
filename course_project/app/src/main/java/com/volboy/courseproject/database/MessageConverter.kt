package com.volboy.courseproject.database

import androidx.room.TypeConverter
import com.volboy.courseproject.model.ReactionsJSON

class MessageConverter {
    @TypeConverter
    fun fromReactions(listReactions: List<ReactionsJSON>): String {
        val stringBuilder: StringBuilder = StringBuilder("")
        listReactions.forEach { r ->
            stringBuilder.append(
                "?${r.emojiCode},${r.emojiName},${r.reactionType},${r.userId},"
            )
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toReactions(stringReactions: String): List<ReactionsJSON> {
        val listReactions = mutableListOf<ReactionsJSON>()
        if (stringReactions.isNotEmpty()) {
            val stringList = stringReactions.split(",")
            val stringListGrouped = stringList.groupBy { "?" }
            stringListGrouped.forEach { (_, list) ->
                listReactions.add(ReactionsJSON(list[0].removePrefix("?"), list[1], list[2], list[3].toInt()))
            }
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