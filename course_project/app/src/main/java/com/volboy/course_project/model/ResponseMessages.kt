package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.volboy.course_project.database.MessageConverter

class Narrow(val operator: String, val operand: String)

class MessageResponse(
    val anchor: Long,
    val found_newest: Boolean,
    val found_oldest: Boolean,
    val history_limited: Boolean,
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
    val avatar_url: String,
    val client: String,
    val content: String,
    val content_type: String,
    val display_recipient: String,
    @PrimaryKey
    val id: Int,
    val is_me_message: Boolean,
    val reactions: List<ReactionsJSON>,
    val recipient_id: Int,
    val sender_email: String,
    val sender_full_name: String,
    val sender_id: Int,
    val sender_realm_str: String,
    val stream_id: Int,
    val subject: String,
    val topic_links: Array<String>,
    val submessages: Array<String>,
    val timestamp: Long,
    val type: String,
    val flags: Array<String>,
    val last_edit_timestamp: Int,
    val match_content: String?,
    val match_subject: String?
)

class ReactionsJSON(
    val emoji_code: String,
    val emoji_name: String,
    val reaction_type: String,
    val user_id: Int
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
    val full_name: String,
    val is_mirror_dummy: Boolean
)
