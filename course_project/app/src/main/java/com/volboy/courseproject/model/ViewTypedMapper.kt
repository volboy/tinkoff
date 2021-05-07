package com.volboy.courseproject.model

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.volboy.courseproject.App
import com.volboy.courseproject.MainPresenter
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.presentation.messages.DataUi
import com.volboy.courseproject.presentation.messages.ReactionsUi
import com.volboy.courseproject.presentation.messages.TextUi
import com.volboy.courseproject.recyclerview.ViewTyped
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ViewTypedMapper {

    @Inject
    lateinit var res: ResourceProvider

    init {

        App.component.injectResourceProvider(this)
    }

    fun groupedMessages(messagesJSON: List<MessageJSON>): List<ViewTyped> = messagesJSON
        .groupBy { getDateTime(it.timestamp) }
        .flatMap { (date, msg) ->
            listOf(DataUi(date, R.layout.item_date_divider)) + viewTypedMessages(msg)
        }

    fun groupedStreamMessages(messagesJSON: List<MessageJSON>): List<ViewTyped> = messagesJSON
        .groupBy { getDateTime(it.timestamp) }
        .flatMap { (date, msg) ->
            listOf(DataUi(date, R.layout.item_date_divider)) + groupByTopic(msg)
        }

    private fun groupByTopic(messagesJSON: List<MessageJSON>) = messagesJSON
        .groupBy { it.subject }
        .flatMap { (topic, msg) ->
            listOf(DataUi(topic, R.layout.item_date_divider)) + viewTypedMessages(msg)
        }

    private fun viewTypedMessages(messagesJSON: List<MessageJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        messagesJSON.forEach { msg ->
            if (msg.senderId != MainPresenter.ownId) {
                viewTypedList.add(
                    TextUi(
                        msg.senderFullName,
                        formatText(msg.content),
                        msg.avatarUrl,
                        R.layout.item_in_message,
                        msg.id.toString()
                    )
                )
                viewTypedList.add(
                    ReactionsUi(
                        recountReactions(msg.reactions),
                        R.layout.item_messages_reactions,
                        msg.id.toString() + msg.reactions.toString()
                    )
                )
            } else {
                viewTypedList.add(
                    TextUi(
                        res.getString(R.string.you_str),
                        formatText(msg.content),
                        msg.avatarUrl,
                        R.layout.item_out_message,
                        msg.id.toString()
                    )
                )
                viewTypedList.add(
                    ReactionsUi(
                        recountReactions(msg.reactions),
                        R.layout.item_messages_reactions_out,
                        msg.id.toString() + msg.reactions.toString()
                    )
                )
            }
        }
        return viewTypedList
    }

    private fun getDateTime(seconds: Long): String {
        val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
        return formatter.format(seconds * 1000)
    }

    private fun formatText(str: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(str)
        }
    }

    private fun recountReactions(reactionsJSON: List<ReactionsJSON>): MutableList<Reaction> {
        val reactions = mutableListOf<Reaction>()
        val reactionsByEmojiCode = reactionsJSON.groupBy { it.emojiCode }
        reactionsByEmojiCode.forEach { (emojiCode, list) ->
            val users = mutableListOf<Int>()
            list.forEach {
                users.add(it.userId)
            }
            reactions.add(Reaction(emojiCode, list[0].emojiName, list.size, list[0].reactionType, users))

        }
        return reactions
    }
}