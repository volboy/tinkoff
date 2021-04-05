package com.volboy.course_project.model

class TopicResponse(val msg: String, val result: String, val topics: List<TopicJSON>)

class TopicJSON(
    val max_id: Int,
    val name: String
)