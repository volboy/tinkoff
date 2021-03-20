package com.volboy.course_project.model

class Message(
    val id: Int,
    val sender: String,
    val textMessage: String,
    val inMessage: Boolean,
    val dateMessage: String,
    val reactions: MutableList<Reaction>?
)