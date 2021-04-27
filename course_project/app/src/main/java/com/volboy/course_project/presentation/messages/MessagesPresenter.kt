package com.volboy.course_project.presentation.messages

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loaderMessages
import com.volboy.course_project.MainActivity.Companion.ownId
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ReactionsUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var isNextLoadingMessage = false
    private var lastMsgId = 0
    private lateinit var data: MutableList<ViewTyped>
    private lateinit var newData: MutableList<ViewTyped>
    private lateinit var reactionsOfMessage: MutableList<Reaction>

    fun loadFirstRemoteMessages(streamName: String, topicName: String) {
        val messages = loaderMessages.getMessages(streamName, topicName)
        messages.subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showMessage(data)
                lastMsgId = result[result.lastIndex - 1].uid.toInt()
                isNextLoadingMessage = true
                writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(App.resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    fun loadNextRemoteMessages(streamName: String, topicName: String, lastMsgIdInTopic: String) {
        if ((data.firstOrNull { item -> item.uid == lastMsgIdInTopic }) == null) {
            newData = data //нужна копия, а не ссылка
            newData.add(ProgressItem)
            view.showMessage(newData)
            val messages = loaderMessages.getMessagesNext(lastMsgId, streamName, topicName)
            messages.subscribe(
                { result ->
                    lastMsgId = result[result.lastIndex - 1].uid.toInt()
                    data.addAll(result)
                    view.showMessage(data)
                    writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
                },
                { error ->
                    view.showError(error.message)
                    writeLog(App.resourceProvider.getString(R.string.msg_network_error))
                }
            ).disposeOnFinish()
        }
    }

    fun addOrDeleteReaction(positionMsgOnLongClick: Int, emojiList: ArrayList<String>) {
        var indexOfReaction = 0
        reactionsOfMessage = (data[positionMsgOnLongClick] as ReactionsUi).reactions
        val isFindEmoji = reactionsOfMessage.firstOrNull { String(Character.toChars(it.emojiCode.toInt(16))) == emojiList[1] }
        if (isFindEmoji != null) {
            val isFindUser = isFindEmoji.users.firstOrNull { it == ownId }
            if (isFindUser != null) {
                indexOfReaction = reactionsOfMessage.indexOf(isFindEmoji)
                removeReaction(
                    (data[positionMsgOnLongClick - 1] as TextUi).uid.toInt(),
                    emojiList[0],
                    "unicode_emoji",
                    indexOfReaction,
                    positionMsgOnLongClick
                )
            } else {
                addReaction(
                    (data[positionMsgOnLongClick - 1] as TextUi).uid.toInt(),
                    emojiList[0],
                    "unicode_emoji",
                    indexOfReaction,
                    null,
                    positionMsgOnLongClick
                )
            }
        } else {
            val newReaction = Reaction(emojiList[1], 1, "unicode_emoji", mutableListOf(ownId))
            addReaction(
                (data[positionMsgOnLongClick - 1] as TextUi).uid.toInt(),
                emojiList[0],
                "unicode_emoji",
                indexOfReaction,
                newReaction,
                positionMsgOnLongClick
            )
        }
    }

    private fun removeReaction(messageId: Int, emojiName: String, reactionType: String, indexOfReaction: Int, positionMsgOnLongClick: Int) {
        val removeEmoji = loaderMessages.removeEmojiFromMessage(messageId, emojiName, reactionType)
        removeEmoji.subscribe(
            { result ->
                (data[positionMsgOnLongClick] as ReactionsUi).reactions[indexOfReaction].users.remove(ownId)
                view.showMessage(data)
            },
            { error ->
                //TODO отображаем сообщение с ошибкой
            }
        ).disposeOnFinish()
    }

    private fun addReaction(
        messageId: Int,
        emojiName: String,
        reactionType: String,
        indexOfReaction: Int,
        newReaction: Reaction? = null,
        positionMsgOnLongClick: Int
    ) {
        val addEmoji = loaderMessages.addEmojiToMessage(messageId, emojiName, reactionType)
        addEmoji.subscribe(
            { result ->
                if (newReaction != null) {
                    (data[positionMsgOnLongClick] as ReactionsUi).reactions.add(newReaction)
                } else {
                    (data[positionMsgOnLongClick] as ReactionsUi).reactions[indexOfReaction].users.add(ownId)
                }
                view.showMessage(data)
            },
            { error ->
                //TODO отображаем сообщение с ошибкой
            }
        ).disposeOnFinish()
    }


    private fun writeLog(msg: String) {
        Log.i(App.resourceProvider.getString(R.string.log_string), msg)
    }
}
