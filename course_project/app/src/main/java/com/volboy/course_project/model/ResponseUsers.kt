package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.volboy.course_project.database.UsersConverter

class UsersResponse(val members: List<UserJSON>)

@Entity
@TypeConverters(UsersConverter::class)
class UserJSON(
    val email: String,
    @SerializedName("is_bot")
    val is_bot: Boolean,
    @SerializedName("avatar_url")
    val avatar_url: String,
    @SerializedName("avatar_version")
    val avatarVersion: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    @SerializedName("is_owner")
    val isOwner: Boolean,
    @SerializedName("bot_type")
    val botType: Int,
    @PrimaryKey
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("bot_owner_id")
    val botOwnerId: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_guest")
    val isGuest: Boolean,
    val timezone: String,
    @SerializedName("date_joined")
    val dateJoined: String,
    @SerializedName("delivery_email")
    val deliveryEmail: String,
    @SerializedName("profile_data")
    val profileData: List<IdData>
)

class IdData(val id: List<ProfileData>)

class ProfileData(val value: String, @SerializedName("rendered_value") val renderedValue: String)

class OwnUser(
    @SerializedName("avatar_url")
    val avatar_url: String,
    @SerializedName("avatar_version")
    val avatar_version: Int,
    val email: String,
    @SerializedName("full_name")
    val full_name: String,
    @SerializedName("is_admin")
    val is_admin: Boolean,
    @SerializedName("is_owner")
    val is_owner: Boolean,
    @SerializedName("is_guest")
    val is_guest: Boolean,
    @SerializedName("is_bot")
    val is_bot: Boolean,
    @SerializedName("is_active")
    val is_active: Boolean,
    val timezone: String,
    @SerializedName("date_joined")
    val date_joined: String,
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("delivery_email")
    val delivery_email: String,
    val id: List<IdData>
)

class StatusUserResponse(val result: String, val msg: String, val presence: Presence)

class Presence(
    val aggregated: Aggregated
)

class Aggregated(
    val status: String,
    val timestamp: Int
)