package com.volboy.courseproject.presentation.messages

import android.util.Log
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.MainPresenter.Companion.ownId
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.model.Reaction
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.ProgressItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var lastItemId = 0
    private lateinit var data: MutableList<ViewTyped>
    private lateinit var reactionsOfMessage: MutableList<Reaction>

    @Inject
    lateinit var loaderMessages: LoaderMessage

    @Inject
    lateinit var res: ResourceProvider

    @Inject
    lateinit var appDatabase: AppDatabase

    init {
        component.injectLoaderMessages(this)
        component.injectResourceProvider(this)
        component.injectDatabase(this)
    }

    fun loadFirstRemoteMessages(streamName: String, topicName: String) {
        appDatabase.messagesDao().getAllMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { msg -> loaderMessages.groupedMessages(msg) }
            .subscribe { viewTypedMsg ->
                data = viewTypedMsg as MutableList<ViewTyped>
            }.disposeOnFinish()
        val messages = loaderMessages.getMessages(streamName, topicName)
        messages.subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showMessage(data, 0)
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(res.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    fun loadNextRemoteMessages(streamName: String, topicName: String) {
        val lastItem = data.lastOrNull { item -> item.viewType == R.layout.item_in_message || item.viewType == R.layout.item_out_message }
        var msgIndex = 0
        if (lastItem != null) {
            msgIndex = data.indexOf(lastItem)
            lastItemId = lastItem.uid.toInt()
        }
        val buffer = ArrayList(data)
        buffer.add(msgIndex + 2, ProgressItem)
        view.showMessage(buffer, buffer.size - 1)
        val messages = loaderMessages.getMessagesNext(lastItemId, streamName, topicName)
        messages.subscribe(
            { result ->
                val resultData = ArrayList(buffer)
                with(resultData) {
                    addAll(msgIndex + 2, result)
                    removeAt(msgIndex + 1) //удаляем реакции под старым сообщением
                    if (resultData[msgIndex - 1].uid == "date") {
                        removeAt(msgIndex - 1)
                    }
                    removeAt(msgIndex) //удаляем старое сообщение
                    remove(ProgressItem)
                }
                view.showMessage(resultData, msgIndex)
                data = resultData
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(res.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()

    }

    fun addOrDeleteReaction(indexMsg: Int, emojiList: ArrayList<String>) {
        var positionEmoji = -1
        val msgID = (data[indexMsg + 1] as TextUi).uid.toInt()
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

    fun sendMessage(str: String, streamName: String, topicName: String) {
        val sendMessage = loaderMessages.sendMessage(str, streamName, topicName)
        sendMessage.subscribe(
            { result ->
                val getLastMessage = loaderMessages.getLastMessage(result.id, streamName, topicName)
                getLastMessage.subscribe(
                    { lastMsg ->
                        data.addAll(0, lastMsg)
                        view.sendMessage(data, 0)
                    },
                    { error ->
                        //TODO отображаем сообщение с ошибкой
                    }
                ).disposeOnFinish()
            },
            { error ->
                //TODO отображаем сообщение с ошибкой
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(res.getString(R.string.log_string), msg)
    }

    private companion object {
        const val UNICODE_EMOJI = "unicode_emoji"
    }
}
