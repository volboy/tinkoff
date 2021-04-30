package com.volboy.courseproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class TopicResponse(val msg: String, val result: String, val topics: List<TopicJSON>)

@Entity
class TopicJSON(
    @PrimaryKey
    @SerializedName("max_id")
    val maxId: Int,
    val name: String
)