package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.volboy.course_project.database.MessageConverter

class Narrow(val operator: String, val operand: String)

class MessageResponse(
    val anchor: Long,
    @SerializedName("found_newest")
    val foundNewest: Boolean,
    @SerializedName("found_oldest")
    val foundOldest: Boolean,
    @SerializedName("history_limited")
    val historyLimited: Boolean,
    val messages: List<MessageJSON>
)

class AddReactionResponse(
    val msg: String,
    val result: String
)

class SendMessageResponse(
    id: Int,
    deliver_at: String
)

class UpdateMessageFlag(
    val messages: List<Long>,
    val msg: String,
    val result: String
)


@Entity
@TypeConverters(MessageConverter::class)
class MessageJSON(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val client: String,
    val content: String,
    val content_type: String,
    @SerializedName("display_recipient")
    val displayRecipient: String,
    @PrimaryKey
    val id: Int,
    val isMeMessage: Boolean,
    val reactions: List<ReactionsJSON>,
    @SerializedName("recipient_id")
    val recipientId: Int,
    @SerializedName("sender_email")
    val senderEmail: String,
    @SerializedName("sender_full_name")
    val senderFullName: String,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("sender_realm_str")
    val senderRealmStr: String,
    @SerializedName("stream_id")
    val streamId: Int,
    val subject: String,
    @SerializedName("topic_links")
    val topicLinks: Array<String>,
    val submessages: Array<String>,
    val timestamp: Long,
    val type: String,
    val flags: Array<String>,
    @SerializedName("last_edit_timestamp")
    val lastEditTimestamp: Int,
    @SerializedName("match_content")
    val matchContent: String?,
    @SerializedName("match_subject")
    val matchSubject: String?
)

class ReactionsJSON(
    @SerializedName("emoji_code")
    val emojiCode: String,
    @SerializedName("emoji_name")
    val emojiName: String,
    @SerializedName("reaction_type")
    val reactionType: String,
    @SerializedName("user_id")
    val userId: Int
)

class Reaction(
    val emojiCode: String,
    val count: Int,
    val type: String,
    val users: MutableList<Int>
)

class UserInReactionsJSON(
    val id: Int,
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("is_mirror_dummy")
    val isMirrorDummy: Boolean
)
