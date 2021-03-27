package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.DataUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import io.reactivex.Single

class ObservableMessages() {

    fun getMessages(): Single<List<ViewTyped>> =
        Single.fromCallable { generateMessage() }
            .map { messages -> groupedMessages(messages) }

    private fun generateMessage(): List<Message> {
        val emoji1 = String(Character.toChars(0x1F60C))
        val emoji2 = String(Character.toChars(0x1F60F))
        return mutableListOf(
            Message(1, "Alice Moore", "Привет", true, "12 Мар", null),
            Message(2, "You", "Привет", false, "12 Мар", null),
            Message(3, "Alice Moore", "Как дела?", true, "12 Мар", null),
            Message(4, "You", "Хорошо", false, "12 Мар", mutableListOf(Reaction(emoji1, "Alice Moore", 3))),
            Message(5, "Alice Moore", "Что нового?", true, "13 Мар", mutableListOf(Reaction(emoji2, "You", 31))),
            Message(6, "Alice Moore", "У меня вот, новый корабль", true, "13 Мар", null),
            Message(7, "You", "Все хорошо", false, "14 Мар", null),
            Message(8, "You", "А у меня новый самолет", false, "14 Мар", null)
        )
    }

    private fun groupedMessages(messages: List<Message>): List<ViewTyped> {
        val messageByDate: Map<String, List<Message>> = messages.groupBy { it.dateMessage }
        return messageByDate.flatMap { (date, msg) ->
            listOf(DataUi(date, R.layout.date_divider_item)) + viewTypedMessages(msg)
        }
    }

    private fun viewTypedMessages(messages: List<Message>): List<ViewTyped> {
        val typedList = mutableListOf<ViewTyped>()
        messages.forEach { msg ->
            if (msg.inMessage) {
                typedList.add(TextUi(msg.sender, msg.textMessage, msg.reactions, R.layout.in_message_item, msg.textMessage))
            } else {
                typedList.add(TextUi(msg.sender, msg.textMessage, msg.reactions, R.layout.out_message_item, msg.textMessage))
            }
        }
        return typedList
    }

}