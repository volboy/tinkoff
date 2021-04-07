package com.volboy.course_project.model

class UsersResponse(val members: List<UserJSON>)

class UserJSON(
    val email: String,
    val is_bot: Boolean,
    val avatar_url: String,
    val avatar_version: Int,
    val full_name: String,
    val is_admin: Boolean,
    val is_owner: Boolean,
    val bot_type: Int,
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