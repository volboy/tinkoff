package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class TopicResponse(val msg: String, val result: String, val topics: List<TopicJSON>)

@Entity
class TopicJSON(
    @PrimaryKey
    val max_id: Int,
    val name: String
)