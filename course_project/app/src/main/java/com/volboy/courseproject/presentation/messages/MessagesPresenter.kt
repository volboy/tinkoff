package com.volboy.courseproject.presentation.messages

import android.util.Log
import androidx.core.text.toSpanned
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
import javax.inject.Inject

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var lastItemId = 0
    private var prevLastItemId = 0
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

    fun loadFirstRemoteMessages(streamName: String, topicName: String, streamId: Int) {
        loaderMessages.getMessageFromDB(streamId)
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
        if (lastItemId != prevLastItemId) {
            val buffer = ArrayList(data)
            buffer.add(ProgressItem)
            view.showMessage(buffer, buffer.size - 1)
            val messages = loaderMessages.getMessagesNext(lastItemId, streamName, topicName)
            messages.subscribe(
                { result ->
                    val resultData = ArrayList(buffer)
                    resultData.remove(ProgressItem)
                    data = resultData.union(result).toMutableList()
                    view.showMessage(data, msgIndex)
                    prevLastItemId = lastItemId
                    writeLog(res.getString(R.string.msg_network_ok))
                },
                {
                    view.showMessage(data, msgIndex)
                    writeLog(res.getString(R.string.msg_network_error))
                }
            ).disposeOnFinish()
        }
    }

    fun deleteMessage(messageId: Int, position: Int) {
        loaderMessages.deleteMessage(messageId).subscribe(
            { response ->
                if (response.result == SUCCESS_RESULT) {
                    data.removeAt(position)
                    view.deleteMessage(data, position)
                } else {
                    view.showInfo(res.getString(R.string.something_wrong), response.msg)
                }
            },
            { error -> view.showInfo(res.getString(R.string.something_wrong), error.message.toString()) }
        ).disposeOnFinish()
    }

    fun editMessage(messageId: Int, position: Int, content: String) {
        loaderMessages.editMessage(messageId, content).subscribe(
            { response ->
                if (response.result == SUCCESS_RESULT) {
                    (data[position] as TextUi).message = content.toSpanned()
                    view.updateMessage(data, position)
                } else {
                    view.showInfo(res.getString(R.string.something_wrong), response.msg)
                }
            },
            { error -> view.showInfo(res.getString(R.string.something_wrong), error.message.toString()) }
        ).disposeOnFinish()
    }

    fun changeTopicOfStream(messageId: Int, position: Int, topic: String) {
        loaderMessages.changeTopicOfMessage(messageId, topic).subscribe(
            { response ->
                if (response.result == SUCCESS_RESULT) {
                    data.removeAt(position)
                    view.deleteMessage(data, position)
                } else {
                    view.showInfo(res.getString(R.string.something_wrong), response.msg)
                }
            },
            { error -> view.showInfo(res.getString(R.string.something_wrong), error.message.toString()) }
        ).disposeOnFinish()
    }

    fun addOrDeleteReaction(indexMsg: Int, emojiList: ArrayList<String>) {
        var positionEmoji = NOT_FIND_EMOJI
        val msgID = (data[indexMsg + 1] as TextUi).uid.toInt()
        val newReaction = Reaction(emojiList[EMOJI_CODE_INDEX], emojiList[EMOJI_NAME_INDEX], 1, UNICODE_EMOJI, mutableListOf(ownId))
        reactionsOfMessage = (data[indexMsg] as ReactionsUi).reactions
        //???????? ???????????? ?????????????? ???????? ?????????????????? ??????????
        if (reactionsOfMessage.isNullOrEmpty()) {
            reactionsOfMessage.add(newReaction)
            addReaction(msgID, emojiList[EMOJI_NAME_INDEX], UNICODE_EMOJI, indexMsg)
        } else {
            //???????? ???????????? ?????????????? ?????????? ???????????????? ?? ???????????? ??????????????
            reactionsOfMessage.forEach { reaction ->
                if (reaction.emojiCode == emojiList[EMOJI_CODE_INDEX]) {
                    positionEmoji = reactionsOfMessage.indexOf(reaction)
                }
            }
            //???????? ???? ?????????? ???????????? ????????????, ?????????? ??????????????????
            if (positionEmoji == NOT_FIND_EMOJI) {
                reactionsOfMessage.add(newReaction)
                addReaction(msgID, emojiList[EMOJI_NAME_INDEX], UNICODE_EMOJI, indexMsg)
                //???????? ?????????? ??????????
            } else {
                //?????????????????? ???????????? ???? ???????????????????????? ?????????? ????????????
                if (ownId in reactionsOfMessage[positionEmoji].users) {
                    reactionsOfMessage[positionEmoji].users.remove(ownId)
                    removeReaction(msgID, emojiList[EMOJI_NAME_INDEX], UNICODE_EMOJI, indexMsg)
                    if (reactionsOfMessage[positionEmoji].users.size == 0) {
                        reactionsOfMessage.removeAt(positionEmoji)
                    }
                } else {
                    reactionsOfMessage[positionEmoji].users.add(ownId)
                    addReaction(msgID, emojiList[EMOJI_NAME_INDEX], UNICODE_EMOJI, indexMsg)
                }
            }
        }
    }

    private fun removeReaction(msgId: Int, emojiName: String, reactionType: String, indexMsg: Int) {
        val removeEmoji = loaderMessages.removeEmojiFromMessage(msgId, emojiName, reactionType)
        removeEmoji.subscribe(
            {
                (data[indexMsg] as ReactionsUi).reactions = reactionsOfMessage
                view.updateMessage(data, indexMsg)
            },
            { error ->
                view.showInfo(res.getString(R.string.something_wrong), error.message.toString())
            }
        ).disposeOnFinish()
    }

    private fun addReaction(msgId: Int, emojiName: String, reactionType: String, indexMsg: Int) {
        val addEmoji = loaderMessages.addEmojiToMessage(msgId, emojiName, reactionType)
        addEmoji.subscribe(
            {
                (data[indexMsg] as ReactionsUi).reactions = reactionsOfMessage
                view.updateMessage(data, indexMsg)
            },
            { error ->
                view.showInfo(res.getString(R.string.something_wrong), error.message.toString())
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
                        view.showInfo(res.getString(R.string.something_wrong), error.message.toString())
                    }
                ).disposeOnFinish()
            },
            { error ->
                view.showInfo(res.getString(R.string.something_wrong), error.message.toString())
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(res.getString(R.string.log_string), msg)
    }

    private companion object {
        const val UNICODE_EMOJI = "unicode_emoji"
        const val SUCCESS_RESULT = "success"
        const val EMOJI_CODE_INDEX = 1
        const val EMOJI_NAME_INDEX = 0
        const val NOT_FIND_EMOJI = -1
    }
}
