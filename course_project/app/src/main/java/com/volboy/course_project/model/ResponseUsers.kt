package com.volboy.course_project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.volboy.course_project.repository.UsersConverter

class UsersResponse(val members: List<UserJSON>)

@Entity
@TypeConverters(UsersConverter::class)
class UserJSON(
    val email: String,
    val is_bot: Boolean,
    val avatar_url: String,
    val avatar_version: Int,
    val full_name: String,
    val is_admin: Boolean,
    val is_owner: Boolean,
    val bot_type: Int,
    @PrimaryKey
    val user_id: Int,
    val bot_owner_id: Int,
    val is_active: Boolean,
    val is_guest: Boolean,
    val timezone: String,
    val date_joined: String,
    val delivery_email: String,
    val profile_data: List<IdData>
)

class IdData(val id: List<ProfileData>)

class ProfileData(val value: String, val rendered_value: String)

class OwnUser(
    val avatar_url: String,
    val avatar_version: Int,
    val email: String,
    val full_name: String,
    val is_admin: Boolean,
    val is_owner: Boolean,
    val is_guest: Boolean,
    val is_bot: Boolean,
    val is_active: Boolean,
    val timezone: String,
    val date_joined: String,
    val user_id: Int,
    val delivery_email: String,
    val id: List<IdData>
)