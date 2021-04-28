package com.volboy.course_project.model

import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.presentation.streams.TitleUi
import com.volboy.course_project.recyclerview.ViewTyped
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LoaderStreams {

    fun getRemoteStreams(): Single<MutableList<ViewTyped>> {
        val streamsDao = App.appDatabase.streamsDao()
        return App.instance.zulipApi.getStreams()
            .subscribeOn(Schedulers.io())
            //TODO("Не забыть убрать, это для проверки загрузки данных из БД)
            .delay(2, TimeUnit.SECONDS)
            .map { response ->
                streamsDao.updateStreams(response.streams)
                viewTypedStreams(response.streams)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun viewTypedStreams(streamsJSON: List<StreamJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        streamsJSON.forEach { streams ->
            val uid = streams.streamId.toString()
            viewTypedList.add(
                TitleUi(
                    streams.name,
                    0,
                    false,
                    null,
                    null,
                    R.drawable.ic_arrow_down,
                    R.layout.item_collapse,
                    uid
                )
            )
        }
        return viewTypedList
    }

    fun getTopicsOfStreams(id: Int): Single<MutableList<ViewTyped>> {
        val topicsDao = App.appDatabase.topicsDao()
        return App.instance.zulipApi.getStreamsTopics(id)
            .subscribeOn(Schedulers.io())
            //TODO Не забыть убрать
            .delay(2, TimeUnit.SECONDS)
            .map { response ->
                topicsDao.updateTopics(response.topics)
                viewTypedTopics(response.topics, id)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun viewTypedTopics(topicsJSON: List<TopicJSON>, streamId: Int): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        topicsJSON.forEach { topic ->
            val uid = topic.maxId
            viewTypedList.add(
                TitleUi(
                    topic.name,
                    0,
                    false,
                    null,
                    streamId,
                    0,
                    R.layout.item_expand,
                    uid.toString()
                )
            )
        }
        return viewTypedList
    }
}