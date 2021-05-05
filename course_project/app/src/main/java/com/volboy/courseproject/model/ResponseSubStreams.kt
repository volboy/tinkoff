package com.volboy.courseproject.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class SubStreamResponse(val subscriptions: List<SubStreamJSON>)

class SubStreamJSON(
    @PrimaryKey
    @SerializedName("stream_id")
    val streamId: Int,
    val name: String,
    val description: String,
    @SerializedName("rendered_description")
    val renderedDescription: String,
    @SerializedName("date_created")
    val dateCreated: Int,
    @SerializedName("invite_only")
    val inviteOnly: Boolean,
    val subscribers: Array<Int>,
    @SerializedName("desktop_notifications")
    val desktopNotifications: Boolean,
    @SerializedName("emailNotifications")
    val email_notifications: Boolean,
    @SerializedName("wildcard_mentions_notify")
    val wildcardMentionsNotify: Boolean,
    @SerializedName("push_notifications")
    val pushNotifications: Boolean,
    @SerializedName("audibleNotifications")
    val audible_notifications: Boolean,
    @SerializedName("pin_to_top")
    val pinToTop: Boolean,
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("is_muted")
    val isMuted: Boolean,
    @SerializedName("in_home_view")
    val inHomeView: Boolean,
    @SerializedName("is_announcement_only")
    val isAnnouncementOnly: Boolean,
    @SerializedName("is_web_public")
    val isWebPublic: Boolean,
    val role: Int,
    val color: String,
    @SerializedName("stream_post_policy")
    val streamPostPolicy: Int,
    @SerializedName("message_retention_days")
    val messageRetentionDays: Int,
    @SerializedName("history_public_to_subscribers")
    val historyPublicSubscribers: Boolean,
    @SerializedName("first_message_id")
    val firstMessageId: Int,
    @SerializedName("stream_weekly_traffic")
    val streamWeeklyTraffic: Int,
)