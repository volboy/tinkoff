package com.volboy.hw_2.model

import com.volboy.hw_2.R
import com.volboy.hw_2.message_recycler_view.DataUi
import com.volboy.hw_2.message_recycler_view.TextUi
import com.volboy.hw_2.message_recycler_view.ViewTyped

class LoaderMessage {
    fun remoteMessage(): List<ViewTyped> {
        val messages=mutableListOf(
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
                viewTypedList.add(TextUi(msg.sender, msg.textMessage, R.layout.in_message_item))
            } else {
                viewTypedList.add(TextUi(msg.sender, msg.textMessage, R.layout.out_message_item))
            }
        }
        return viewTypedList
    }
}