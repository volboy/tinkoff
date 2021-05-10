package com.volboy.courseproject.model

import com.google.gson.Gson
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.api.ZulipApi
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.recyclerview.ViewTyped
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoaderMessage {
    private val converter = Gson()
    private val viewTypedMapper = MessageMapper()

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var zulipApi: ZulipApi

    init {
        component.injectDatabase(this)
        component.injectRetrofit(this)
    }

    fun getMessages(streamName: String, topicName: String): Single<List<ViewTyped>> {
        val messagesDao = appDatabase.messagesDao()
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val narrowsJSON = converter.toJson(narrows)
        return zulipApi.getMessages("newest", 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                messagesDao.updateMessages(response.messages)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedMapper.groupedMessages(response.messages)
            }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMessagesNext(startId: Int, streamName: String, topicName: String): Single<List<ViewTyped>> {
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val narrowsJSON = converter.toJson(narrows)
        return zulipApi.getMessagesNext(startId, 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            //TODO("Не забыть убрать, это для проверки пагинации)
            .delay(2, TimeUnit.SECONDS)
            .map { response -> viewTypedMapper.groupedMessages(response.messages) }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMessageFromDB(streamId: Int): Maybe<List<ViewTyped>> {
        return appDatabase.messagesDao().getAllMessages(streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { msg -> viewTypedMapper.groupedMessages(msg) }
    }

    fun getMessageOfStreamFromDB(streamId: Int): Maybe<List<ViewTyped>> {
        return appDatabase.messagesDao().getAllMessages(streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { msg -> viewTypedMapper.groupedStreamMessages(msg) }
    }

    fun getStreamMessages(streamName: String): Single<List<ViewTyped>> {
        val messagesDao = appDatabase.messagesDao()
        val narrows = listOf(Narrow("stream", streamName))
        val narrowsJSON = converter.toJson(narrows)
        return zulipApi.getMessages("newest", 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                messagesDao.updateMessages(response.messages)
                    .andThen(Single.just(response))
            }
            .map { response ->
                viewTypedMapper.groupedStreamMessages(response.messages)
            }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStreamMessagesNext(startId: Int, streamName: String): Single<List<ViewTyped>> {
        val narrows = listOf(Narrow("stream", streamName))
        val narrowsJSON = converter.toJson(narrows)
        return zulipApi.getMessagesNext(startId, 20, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .map { response ->
                viewTypedMapper.groupedStreamMessages(response.messages)
            }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTopicsOfStreams(id: Int): Single<List<String>> {
        return zulipApi.getStreamsTopics(id)
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.topics.map { topic ->
                    topic.name
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLastMessage(startId: Int, streamName: String, topicName: String): Single<List<ViewTyped>> {
        val narrows = listOf(Narrow("stream", streamName), Narrow("topic", topicName))
        val converter = Gson()
        val narrowsJSON = converter.toJson(narrows)
        return zulipApi.getMessagesNext(startId, 0, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .map { response -> viewTypedMapper.groupedMessages(response.messages) }
            .map { list -> list.reversed() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addEmojiToMessage(messageId: Int, emojiName: String, reactionType: String): Single<AddReactionResponse> =
        zulipApi.addReaction(messageId, emojiName, reactionType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun removeEmojiFromMessage(messageId: Int, emojiName: String, reactionType: String): Single<AddReactionResponse> =
        zulipApi.removeReaction(messageId, emojiName, reactionType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun deleteMessage(messageId: Int): Single<DeleteMessageResponse> =
        zulipApi.deleteMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun editMessage(messageId: Int, content: String) =
        zulipApi.editMessage(messageId, content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun changeTopicOfMessage(messageId: Int, topic: String) =
        zulipApi.changeTopicOfMessage(messageId, topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun sendMessage(str: String, streamName: String, topicName: String): Single<SendMessageResponse> =
        zulipApi.sendMessage("stream", streamName, str, topicName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
