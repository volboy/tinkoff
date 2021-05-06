package com.volboy.courseproject.model

import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.api.ZulipApi
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.presentation.streams.allstreams.AllStreamsUi
import com.volboy.courseproject.presentation.streams.mystreams.TitleUi
import com.volboy.courseproject.recyclerview.ViewTyped
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun getRemoteSubStreams(): Single<List<ViewTyped>> {
        val subStreamsDao = appDatabase.subStreamsDao()
        return zulipApi.getSubscribedStreams()
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                subStreamsDao.updateSubStreams(response.subscriptions)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedSubStreams(response.subscriptions)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getRemoteAllStreams(): Single<List<ViewTyped>> {
        val streamsDao = appDatabase.streamsDao()
        return zulipApi.getStreams()
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                streamsDao.updateStreams(response.streams)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedStreams(response.streams)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTopicsOfStreams(id: Int): Single<MutableList<ViewTyped>> {
        val topicsDao = appDatabase.topicsDao()
        return zulipApi.getStreamsTopics(id)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                val topicsForDB = mutableListOf<TopicForDB>()
                response.topics.forEach { topic ->
                    topicsForDB.add(TopicForDB(topic.maxId, topic.name, id))
                }
                topicsDao.updateTopics(topicsForDB)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedTopics(response.topics, id)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTopicsOfStreamsFromDB(id: Int): Maybe<MutableList<ViewTyped>> {
        return appDatabase.topicsDao().getTopicsOfStream(id)
            .subscribeOn(Schedulers.io())
            .map { topics ->
                val topicsJSON = mutableListOf<TopicJSON>()
                topics.forEach { topic ->
                    topicsJSON.add(TopicJSON(topic.maxId, topic.name))
                }
                viewTypedTopics(topicsJSON, id)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSubStreamsFromDB(): Maybe<List<ViewTyped>> {
        return appDatabase.subStreamsDao().getSubStreams()
            .subscribeOn(Schedulers.io())
            .map { streams ->
                viewTypedSubStreams(streams)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getAllStreamsFromDB(): Maybe<List<ViewTyped>> {
        return appDatabase.streamsDao().getAllStreams()
            .subscribeOn(Schedulers.io())
            .map { streams ->
                viewTypedStreams(streams)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun subscribeToStream(subscriptions: String, inviteOnly: Boolean): Single<SubscribedJSON> {
        return zulipApi.subscribeToStream(subscriptions, inviteOnly)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun unSubscribeToStream(subscriptions: String): Single<SubscribedJSON> {
        return zulipApi.unSubscribeToStream(subscriptions)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun viewTypedSubStreams(subStreamsJSON: List<SubStreamJSON>): List<ViewTyped> = subStreamsJSON
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

    private fun viewTypedStreams(streamsJSON: List<StreamJSON>): List<ViewTyped> = streamsJSON
        .map { stream ->
            AllStreamsUi(
                stream.name,
                false,
                R.layout.item_subto_stream,
                stream.streamId.toString()
            )
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