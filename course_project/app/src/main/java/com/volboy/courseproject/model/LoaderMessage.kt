package com.volboy.courseproject.model

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.google.gson.Gson
import com.volboy.courseproject.App
import com.volboy.courseproject.MainActivity.Companion.ownId
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.messages.DataUi
import com.volboy.courseproject.presentation.messages.ReactionsUi
import com.volboy.courseproject.presentation.messages.TextUi
import com.volboy.courseproject.recyclerview.ViewTyped
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LoaderMessage {

    fun getMessages(streamName: String, topicName: String): Single<List<ViewTyped>> {
        val messagesDao = App.appDatabase.messagesDao()
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val gson = Gson()
        val narrowsJSON = gson.toJson(narrows)
        return App.instance.zulipApi.getMessages("newest", 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .map { response ->
                messagesDao.updateMessages(response.messages)
                groupedMessages(response.messages)
            }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMessagesNext(startId: Int, streamName: String, topicName: String): Single<List<ViewTyped>> {
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val gson = Gson()
        val narrowsJSON = gson.toJson(narrows)
        return App.instance.zulipApi.getMessagesNext(startId, 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            //TODO("Не забыть убрать, это для проверки пагинации)
            .delay(2, TimeUnit.SECONDS)
            .map { response -> groupedMessages(response.messages) }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLastMessage(startId: Int, streamName: String, topicName: String): Single<List<ViewTyped>> {
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val gson = Gson()
        val narrowsJSON = gson.toJson(narrows)
        return App.instance.zulipApi.getMessagesNext(startId, 0, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .map { response -> groupedMessages(response.messages) }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addEmojiToMessage(messageId: Int, emojiName: String, reactionType: String): Single<AddReactionResponse> =
        App.instance.zulipApi.addReaction(messageId, emojiName, reactionType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun removeEmojiFromMessage(messageId: Int, emojiName: String, reactionType: String): Single<AddReactionResponse> =
        App.instance.zulipApi.removeReaction(messageId, emojiName, reactionType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun sendMessage(str: String, streamName: String, topicName: String): Single<SendMessageResponse> =
        App.instance.zulipApi.sendMessage("stream", streamName, str, topicName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun groupedMessages(messagesJSON: List<MessageJSON>): List<ViewTyped> = messagesJSON
        .groupBy { getDateTime(it.timestamp) }
        .flatMap { (date, msg) ->
            listOf(DataUi(date, R.layout.item_date_divider)) + viewTypedMessages(msg)
        }


    private fun viewTypedMessages(messagesJSON: List<MessageJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        messagesJSON.forEach { msg ->
            if (msg.senderId != ownId) {
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
                        App.resourceProvider.getString(R.string.you_str),
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