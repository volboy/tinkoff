package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.DataUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped

class LoaderMessage() {
    private var messages = mutableListOf<Message>()
    fun remoteMessage(): List<ViewTyped> {
        messages = mutableListOf(
            Message(1, "Alice Moore", "Привет", true, "12 Мар", null),
            Message(1, "You", "Привет", false, "12 Мар", null),
            Message(1, "Alice Moore", "Как дела?", true, "12 Мар", null),
            Message(1, "You", "Хорошо", false, "12 Мар", null),
            Message(1, "Alice Moore", "Что нового?", true, "13 Мар", null),
            Message(1, "Alice Moore", "У меня вот, новый корабль", true, "13 Мар", null),
            Message(1, "You", "Все хорошо", false, "14 Мар", null),
            Message(1, "You", "А у меня новый самолет", false, "14 Мар", null)
        )

        return convertMessage(messages)
    }

    fun addMessage(newMessage: String?): List<ViewTyped> {
        if (newMessage != null) {
            messages.add(Message(1, "You", newMessage, false, "14 Мар", null))
        }
        return convertMessage(messages)
    }

    private fun convertMessage(messages: MutableList<Message>): List<ViewTyped> {
        val messageByDate = messages.groupBy { it.dateMessage }
        return messageByDate.flatMap { (date, messages) ->
            listOf(DataUi(date)) + parseMessages(messages)
        }
    }

    private fun parseMessages(messages: List<Message>): List<ViewTyped> {
        var viewTypedList: MutableList<ViewTyped> = mutableListOf()
        messages.forEach { msg ->
            if (msg.inMessage) {
                viewTypedList.add(TextUi(msg.sender, msg.textMessage, R.layout.in_message_item, msg.id.toString()))
            } else {
                viewTypedList.add(TextUi(msg.sender, msg.textMessage, R.layout.out_message_item, msg.id.toString()))
            }
        }
        return viewTypedList
    }
}