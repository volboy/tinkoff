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
