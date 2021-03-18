package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.DataUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped

class LoaderMessage() {
    private var viewTypedList: MutableList<ViewTyped> = mutableListOf()
    fun remoteMessage(): List<ViewTyped> {
        val messages = mutableListOf(
            Message(1, "Alice Moore", "Привет", true, "12 Мар", null),
            Message(2, "You", "Привет", false, "12 Мар", null),
            Message(3, "Alice Moore", "Как дела?", true, "12 Мар", null),
            Message(4, "You", "Хорошо", false, "12 Мар", null),
            Message(5, "Alice Moore", "Что нового?", true, "13 Мар", null),
            Message(6, "Alice Moore", "У меня вот, новый корабль", true, "13 Мар", null),
            Message(7, "You", "Все хорошо", false, "14 Мар", null),
            Message(8, "You", "А у меня новый самолет", false, "14 Мар", null)
        )
        viewTypedList = convertMessage(messages).toMutableList()
        return viewTypedList
    }

    fun addMessage(newMessage: String?): List<ViewTyped> {
        val msg: Message
        if (newMessage != null) {
            msg = Message(9, "You", newMessage, false, "14 Мар", null)
            viewTypedList.add(TextUi(msg.sender, msg.textMessage, R.layout.out_message_item, msg.textMessage))
        }
        return viewTypedList
    }

    private fun convertMessage(messages: MutableList<Message>): List<ViewTyped> {
        val messageByDate: Map<String, List<Message>> = messages.groupBy { it.dateMessage }
        return messageByDate.flatMap { (date, msg) ->
            listOf(DataUi(date, R.layout.date_divider_item)) + parseMessages(msg)
        }
    }

    private fun parseMessages(messages: List<Message>): List<ViewTyped> {
        val typedList = mutableListOf<ViewTyped>()
        messages.forEach { msg ->
            if (msg.inMessage) {
                typedList.add(TextUi(msg.sender, msg.textMessage, R.layout.in_message_item, msg.textMessage))
            } else {
                typedList.add(TextUi(msg.sender, msg.textMessage, R.layout.out_message_item, msg.textMessage))
            }
        }
        return typedList
    }
}