package com.volboy.hw_2.model

class Message(
    val id: Int,
    val sender: String,
    val textMessage: String,
    val inMessage: Boolean,
    val dateMessage: String,
    val reactions: List<Reaction>?
)