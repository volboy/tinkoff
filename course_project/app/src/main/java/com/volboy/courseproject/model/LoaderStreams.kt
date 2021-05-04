package com.volboy.courseproject.model

import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.api.ZulipApi
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.presentation.streams.TitleUi
import com.volboy.courseproject.recyclerview.ViewTyped
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoaderStreams {

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var zulipApi: ZulipApi

    init {
        component.injectDatabase(this)
        component.injectRetrofit(this)
    }

    fun getRemoteStreams(): Single<List<ViewTyped>> {
        val streamsDao = appDatabase.streamsDao()
        return zulipApi.getStreams()
            .subscribeOn(Schedulers.io())
            //TODO("Не забыть убрать, это для проверки загрузки данных из БД)
            .delay(2, TimeUnit.SECONDS)
            .flatMap { response ->
                streamsDao.updateStreams(response.streams)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedStreams(response.streams)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun viewTypedStreams(streamsJSON: List<StreamJSON>): List<ViewTyped> = streamsJSON
        .map { stream ->
            TitleUi(
                stream.name,
                0,
                false,
                null,
                R.drawable.ic_arrow_down,
                R.layout.item_collapse,
                stream.streamId.toString()
            )
        }


    fun getTopicsOfStreams(id: Int): Single<MutableList<ViewTyped>> {
        val topicsDao = appDatabase.topicsDao()
        return zulipApi.getStreamsTopics(id)
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