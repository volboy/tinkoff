package com.volboy.course_project.model

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.google.gson.Gson
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.DataUi
import com.volboy.course_project.message_recycler_view.ReactionsUi
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import com.volboy.course_project.ui.people_fragments.PeopleUi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

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

    fun getMessages(streamName: String, topicName: String): Single<List<ViewTyped>> {
        val narrows = listOf<Narrow>(Narrow("stream", streamName), Narrow("topic", topicName))
        val gson = Gson()
        val narrowsJSON = gson.toJson(narrows)
        return App.instance.zulipApi.getMessages("newest", 100, 0, narrowsJSON)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> groupedMessages(response.messages) }
    }

    fun getRemoteUsers(): Single<MutableList<ViewTyped>> {
        return App.instance.zulipApi.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> viewTypedUsers(response.members) }
    }

    fun getOwnUser(): Single<OwnUser> {
        return App.instance.zulipApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun viewTypedStreams(streamsJSON: List<StreamJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        streamsJSON.forEach { streams ->
            val uid = streams.stream_id.toString()
            viewTypedList.add(TitleUi(streams.name, 0, false, null, R.drawable.ic_arrow_down, R.layout.item_collapse, uid))
        }
        return viewTypedList
    }

    fun viewTypedTopics(topicsJSON: List<TopicJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        topicsJSON.forEach { topic ->
            val uid = topic.max_id.toString()
            viewTypedList.add(TitleUi(topic.name, 0, false, null, 0, R.layout.item_expand, topic.name))
        }
        return viewTypedList
    }

    fun groupedMessages(messagesJSON: List<MessageJSON>): List<ViewTyped> {
        val messageByDate: Map<Long, List<MessageJSON>> = messagesJSON.groupBy { it.timestamp }
        return messageByDate.flatMap { (date, msg) ->
            listOf(DataUi(getDateTime(date), R.layout.item_date_divider)) + viewTypedMessages(msg)
        }
    }

    fun viewTypedMessages(messagesJSON: List<MessageJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        messagesJSON.forEach { msg ->
            if (!msg.is_me_message) {
                viewTypedList.add(TextUi(msg.sender_full_name, deleteHtmlFromString(msg.content), msg.avatar_url, R.layout.item_in_message, msg.id.toString()))
                viewTypedList.add(ReactionsUi(recountReactions(msg.reactions), R.layout.item_messages_reactions, msg.reactions.hashCode().toString()))
            } else {
                viewTypedList.add(TextUi("You", deleteHtmlFromString(msg.content), msg.avatar_url, R.layout.item_out_message, msg.id.toString()))
                viewTypedList.add(ReactionsUi(recountReactions(msg.reactions), R.layout.item_messages_reactions_out, msg.reactions.hashCode().toString()))
            }
        }
        return viewTypedList
    }

    private fun getDateTime(seconds: Long): String {
        val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
        return formatter.format(seconds * 1000)
    }

    private fun deleteHtmlFromString(str: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(str)
        }
    }

    fun viewTypedUsers(usersJSON: List<UserJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        usersJSON.forEach { user ->
            val uid = user.user_id.toString()
            viewTypedList.add(PeopleUi(user.full_name, user.email, user.avatar_url, R.layout.item_people_list, uid))
        }
        return viewTypedList
    }

    private fun recountReactions(reactionsJSON: List<ReactionsJSON>): MutableList<Reaction> {
        val reactions = mutableListOf<Reaction>()
        val users = mutableListOf<Int>()
        val reactionsByEmojiCode = reactionsJSON.groupBy { it.emoji_code }
        reactionsByEmojiCode.forEach { (emojiCode, list) ->
            list.forEach {
                users.add(it.user_id)
            }
            reactions.add(Reaction(emojiCode, list.size, list[0].reaction_type, users))
        }
        return reactions
    }
}




