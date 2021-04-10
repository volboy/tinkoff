package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

class StreamResponse(val msg: String, val result: String, val streams: List<StreamJSON>)

@Entity
class StreamJSON(
    @PrimaryKey
    val stream_id: Int,
    val name: String,
    val description: String,
    val invite_only: Boolean,
    val rendered_description: String,
    val is_web_public: Boolean,
    val stream_post_policy: Int,
    val message_retention_days: Int,
    val history_public_to_subscribers: Boolean,
    val first_message_id: Int,
    val is_announcement_only: Boolean,
    val is_default: Boolean
)

