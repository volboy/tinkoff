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

class MessagesOfStreamsPresenter : RxPresenter<MessagesOfStreamView>(MessagesOfStreamView::class.java) {
    private lateinit var data: MutableList<ViewTyped>
    private var lastItemId = 0
    private var prevLastItemId = 0

    @Inject
    lateinit var loaderMessages: LoaderMessage

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderMessages(this)
        App.component.injectResourceProvider(this)
    }

    fun loadAllTopicsOfStream(streamId: Int) {
        loaderMessages.getTopicsOfStreams(streamId).subscribe(
            { result ->
                view.setArrayAdapter(result)
            },
            { error -> writeLog(error.message.toString()) }
        ).disposeOnFinish()
    }

    fun loadMessageOfStream(streamName: String, streamId: Int) {
        loaderMessages.getStreamMessages(streamName).subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showMessage(data, 0)
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showInfo(res.getString(R.string.msg_network_error), error.message.toString())
                loadMsgOfStreamFromDB(streamId)
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
        if (lastItemId != prevLastItemId) {
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

    private fun loadMsgOfStreamFromDB(streamId: Int) {
        loaderMessages.getMessageOfStreamFromDB(streamId)
            .subscribe(
                { viewTypedMsg ->
                    data = viewTypedMsg as MutableList<ViewTyped>
                    view.showMessage(data, 0)
                },
                { error -> view.showInfo(res.getString(R.string.msg_database_error), error.message.toString()) },
                {
                    view.showInfo(
                        res.getString(R.string.msg_database_error),
                        res.getString(R.string.msg_database_empty)
                    )
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


}