package com.volboy.course_project.presentation.messages

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loader
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var isNextLoadingMessage = false
    private var lastMsgId = 0
    private var data = mutableListOf<ViewTyped>()

    fun getMessages(streamName: String, topicName: String) {
        if (isNextLoadingMessage) {
            loadNextRemoteMessages(lastMsgId, streamName, topicName)
            view.showLocalLoading()
        } else {
            loadFirstRemoteMessages(streamName, topicName)
            view.showLoading("")
        }
    }

    private fun loadFirstRemoteMessages(streamName: String, topicName: String) {
        val messages = loader.getMessages(streamName, topicName)
        messages.subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showMessage(data)
                lastMsgId = result[result.lastIndex].uid.toInt()
                isNextLoadingMessage = true
                writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(App.resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun loadNextRemoteMessages(lastMsgId: Int, streamName: String, topicName: String) {
        val messages = loader.getMessagesNext(lastMsgId, streamName, topicName)
        messages.subscribe(
            { result ->
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

    fun sendMessage(msg: String) {

    }

    fun addOrDeleteReaction(msgId: Int) {

    }

    private fun loadMessagesFromDatabase() {

    }

    private fun writeLog(msg: String) {
        Log.i(App.resourceProvider.getString(R.string.log_string), msg)
    }


}