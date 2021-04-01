package com.volboy.course_project.model

class StreamJSON(
    stream_id: Int,
    name: String,
    description: String,
    invite_only: Boolean,
    rendered_description: String,
    is_web_public: Boolean,
    stream_post_policy: Int,
    message_retention_days: Int,
    history_public_to_subscribers: Boolean,
    first_message_id: Int,
    is_announcement_only: Boolean,
    is_default: Boolean
)

class StreamsJSON(result: List<StreamJSON>)
