package com.volboy.course_project.model

class Narrow(operator: String, operand: String)

class MessageResponse(
    val anchor: Int,
    val found_newest: Boolean,
    val found_oldest: Boolean,
    val history_limited: Boolean,
    val messages: List<MessageJSON>
)

class MessageJSON(
    val avatar_url: String,
    val client: String,
    val content: String,
    val content_type: String,
    val display_recipient: String,
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
    val timestamp: Int,
    val type: String,
    val flags: Array<String>,
    val last_edit_timestamp: Int,
    val match_content: String,
    val match_subject: String
)

class ReactionsJSON(
    val emoji_code: String,
    val emoji_name: String,
    val reaction_type: String,
    val user_id: Int,
    val user: List<UserInReactionsJSON>
)

class UserInReactionsJSON(
    val id: Int,
    val email: String,
    val full_name: String,
    val is_mirror_dummy: Boolean
)
