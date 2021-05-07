package com.volboy.courseproject.presentation.msgstream

import android.util.Log
import com.volboy.courseproject.App
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.ProgressItem
import javax.inject.Inject

class AllMessagesPresenter : RxPresenter<AllMessagesView>(AllMessagesView::class.java) {
    private lateinit var data: MutableList<ViewTyped>
    private var lastItemId = 0

    @Inject
    lateinit var loaderMessages: LoaderMessage

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderMessages(this)
        App.component.injectResourceProvider(this)
    }

    fun loadMessageOfStream(streamName: String, streamId: Int) {
        loaderMessages.getMessageFromDB(streamId)
            .subscribe { viewTypedMsg ->
                data = viewTypedMsg as MutableList<ViewTyped>
            }.disposeOnFinish()
        val messages = loaderMessages.getStreamMessages(streamName)
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

    fun loadMessageOfStreamNext(streamName: String) {
        val lastItem = data.lastOrNull { item -> item.viewType == R.layout.item_in_message || item.viewType == R.layout.item_out_message }
        var msgIndex = 0
        if (lastItem != null) {
            msgIndex = data.indexOf(lastItem)
            lastItemId = lastItem.uid.toInt()
        }
        val buffer = ArrayList(data)
        buffer.add(ProgressItem)
        view.showMessage(buffer, buffer.size - 1)
        val messages = loaderMessages.getStreamMessagesNext(lastItemId, streamName)
        messages.subscribe(
            { result ->
                val resultData = ArrayList(buffer)
                resultData.remove(ProgressItem)
                data = resultData.union(result).toMutableList()
                view.showMessage(data, msgIndex)
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(res.getString(R.string.msg_network_error))
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


}