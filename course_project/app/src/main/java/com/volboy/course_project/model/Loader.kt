package com.volboy.course_project.model

import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.DataUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Loader() {

    fun getRemoteStreams(): Single<MutableList<ViewTyped>> {
        return App.instance.zulipApi.getStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> viewTypedStreams(response.streams) }
    }

    fun getTopicsOfStreams(id: Int): Single<MutableList<ViewTyped>> {
        return App.instance.zulipApi.getStreamsTopics(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> viewTypedTopics(response.topics) }
    }

    fun getMessages(): Single<List<ViewTyped>> {
        return App.instance.zulipApi.getMessages("newest", 100, 0, arrayOf(mapOf("stream" to "general"), mapOf("topic" to "test_topic")))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> groupedMessages(response.messages) }
    }

    private fun viewTypedStreams(streamsJSON: List<StreamJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        streamsJSON.forEach { streams ->
            val uid = streams.stream_id.toString()
            viewTypedList.add(TitleUi(streams.name, 0, false, null, R.drawable.ic_arrow_down, R.layout.item_collapse, uid))
        }
        return viewTypedList
    }

    private fun viewTypedTopics(topicsJSON: List<TopicJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        topicsJSON.forEach { topic ->
            val uid = topic.max_id.toString()
            viewTypedList.add(TitleUi(topic.name, 0, false, null, 0, R.layout.item_expand, topic.name))
        }
        return viewTypedList
    }

    private fun groupedMessages(messagesJSON: List<MessageJSON>): List<ViewTyped> {
        val messageByDate: Map<Int, List<MessageJSON>> = messagesJSON.groupBy { it.timestamp }
        return messageByDate.flatMap { (date, msg) ->
            listOf(DataUi(date.toString(), R.layout.item_date_divider)) + viewTypedMessages(msg)
        }
    }

    private fun viewTypedMessages(messagesJSON: List<MessageJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        messagesJSON.forEach { msg ->
            if (msg.is_me_message) {
                viewTypedList.add(TextUi(msg.sender_full_name, msg.content, null, R.layout.item_in_message, msg.id.toString()))
            } else {
                viewTypedList.add(TextUi("You", msg.content, null, R.layout.item_out_message, msg.id.toString()))
            }
        }
        return viewTypedList
    }
}



