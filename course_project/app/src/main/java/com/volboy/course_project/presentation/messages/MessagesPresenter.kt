package com.volboy.course_project.presentation.messages

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loaderMessages
import com.volboy.course_project.MainActivity.Companion.ownId
import com.volboy.course_project.R
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter
import com.volboy.course_project.recyclerview.ViewTyped
import com.volboy.course_project.recyclerview.simple_items.ProgressItem

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var lastItemId = 0
    private lateinit var data: MutableList<ViewTyped>
    private lateinit var reactionsOfMessage: MutableList<Reaction>

    fun loadFirstRemoteMessages(streamName: String, topicName: String) {
        val messages = loaderMessages.getMessages(streamName, topicName)
        messages.subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showMessage(data, 0)
                writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(App.resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    fun loadNextRemoteMessages(streamName: String, topicName: String, lastMsgIdInTopic: String) {
        if (data.firstOrNull { item -> item.uid == lastMsgIdInTopic } == null) {
            val lastItem = data.lastOrNull { item -> item.viewType == R.layout.item_in_message || item.viewType == R.layout.item_out_message }
            var msgIndex = 0
            if (lastItem != null) {
                msgIndex = data.indexOf(lastItem)
                lastItemId = lastItem.uid.toInt()
            }
            val buffer = ArrayList(data)
            buffer.add(msgIndex + 2, ProgressItem)
            view.showMessage(buffer, buffer.size-1)
            val messages = loaderMessages.getMessagesNext(lastItemId, streamName, topicName)
            messages.subscribe(
                { result ->
                    val resultData = ArrayList(buffer)
                    with(resultData) {
                        addAll(msgIndex + 2, result)
                        removeAt(msgIndex+1)
                        removeAt(msgIndex)
                        remove(ProgressItem)
                    }
                    view.showMessage(resultData, msgIndex)
                    data = resultData
                    writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
                },
                { error ->
                    view.showError(error.message)
                    writeLog(App.resourceProvider.getString(R.string.msg_network_error))
                }
            ).disposeOnFinish()
        }
    }

    fun addOrDeleteReaction(indexMsg: Int, emojiList: ArrayList<String>) {
        var positionEmoji = -1
        val msgID = (data[indexMsg - 1] as TextUi).uid.toInt()
        val newReaction = Reaction(emojiList[1], emojiList[0], 1, UNICODE_EMOJI, mutableListOf(ownId))
        reactionsOfMessage = (data[indexMsg] as ReactionsUi).reactions
        //если список реакций пуск добавляем сразу
        if (reactionsOfMessage.isNullOrEmpty()) {
            reactionsOfMessage.add(newReaction)
            addReaction(msgID, emojiList[0], UNICODE_EMOJI, indexMsg)
        } else {
            //ищем эмоджи который хотим добавить в списке реакций
            reactionsOfMessage.forEach { reaction ->
                if (reaction.emojiCode == emojiList[1]) {
                    positionEmoji = reactionsOfMessage.indexOf(reaction)
                }
            }
            //если не нашли такого эмоджи, сразу добавляем
            if (positionEmoji == -1) {
                reactionsOfMessage.add(newReaction)
                addReaction(msgID, emojiList[0], UNICODE_EMOJI, indexMsg)
                //если нашли такой
            } else {
                //проверяем ставил ли пользователь такой эмоджи
                if (ownId in reactionsOfMessage[positionEmoji].users) {
                    reactionsOfMessage[positionEmoji].users.remove(ownId)
                    removeReaction(msgID, emojiList[0], UNICODE_EMOJI, indexMsg)
                    if (reactionsOfMessage[positionEmoji].users.size == 0) {
                        reactionsOfMessage.removeAt(positionEmoji)
                    }
                } else {
                    reactionsOfMessage[positionEmoji].users.add(ownId)
                    addReaction(msgID, emojiList[0], UNICODE_EMOJI, indexMsg)
                }
            }
        }
    }

    private fun removeReaction(msgId: Int, emojiName: String, reactionType: String, indexMsg: Int) {
        val removeEmoji = loaderMessages.removeEmojiFromMessage(msgId, emojiName, reactionType)
        removeEmoji.subscribe(
            { result ->
                (data[indexMsg] as ReactionsUi).reactions = reactionsOfMessage
                view.updateMessage(data, indexMsg)
            },
            { error ->
                //TODO отображаем сообщение с ошибкой
            }
        ).disposeOnFinish()
    }

    private fun addReaction(msgId: Int, emojiName: String, reactionType: String, indexMsg: Int) {
        val addEmoji = loaderMessages.addEmojiToMessage(msgId, emojiName, reactionType)
        addEmoji.subscribe(
            { result ->
                (data[indexMsg] as ReactionsUi).reactions = reactionsOfMessage
                view.updateMessage(data, indexMsg)
            },
            { error ->
                //TODO отображаем сообщение с ошибкой
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(App.resourceProvider.getString(R.string.log_string), msg)
    }

    private companion object {
        const val UNICODE_EMOJI = "unicode_emoji"
    }
}
