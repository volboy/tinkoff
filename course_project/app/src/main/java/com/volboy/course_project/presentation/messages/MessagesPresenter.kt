package com.volboy.course_project.presentation.messages

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loader
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class MessagesPresenter : RxPresenter<MessagesView>(MessagesView::class.java) {
    private var isNextLoadingMessage = false
    private var lastMsgId = 0
    private lateinit var data: MutableList<ViewTyped>
    private lateinit var newData: MutableList<ViewTyped>

    fun loadFirstRemoteMessages(streamName: String, topicName: String) {
        val messages = loader.getMessages(streamName, topicName)
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
            val messages = loader.getMessagesNext(lastMsgId, streamName, topicName)
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