package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class StreamResponse(val msg: String, val result: String, val streams: List<StreamJSON>)

@Entity
class StreamJSON(
    @PrimaryKey
    @SerializedName("stream_id")
    val streamId: Int,
    val name: String,
    val description: String,
    @SerializedName("invite_only")
    val inviteOnly: Boolean,
    @SerializedName("rendered_description")
    val renderedDescription: String,
    @SerializedName("is_web_public")
    val isWebPublic: Boolean,
    @SerializedName("stream_post_policy")
    val streamPostPolicy: Int,
    @SerializedName("message_retention_days")
    val messageRetentionDays: Int,
    @SerializedName("history_public_to_subscribers")
    val historyPublicToSubscribers: Boolean,
    @SerializedName("first_message_id")
    val firstMessageId: Int,
    @SerializedName("is_announcement_only")
    val isAnnouncementOnly: Boolean,
    @SerializedName("is_default")
    val isDefault: Boolean
)

