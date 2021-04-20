package com.volboy.course_project.api

import com.volboy.course_project.model.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ZulipApi {
    @GET("streams?json=true")
    fun getStreams(): Single<StreamResponse>

    @GET("users/me/{id}/topics?json=true")
    fun getStreamsTopics(@Path("id") id: Int): Single<TopicResponse>

    @GET("messages?json=true")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") num_before: Int,
        @Query("num_after") num_after: Int,
        @Query("narrow") narrow: String
    ): Single<MessageResponse>

    @GET("messages?json=true")
    fun getMessagesNext(
        @Query("anchor") anchor: Int,
        @Query("num_before") num_before: Int,
        @Query("num_after") num_after: Int,
        @Query("narrow") narrow: String
    ): Single<MessageResponse>

    @GET("users?json=true")
    fun getUsers(): Single<UsersResponse>

    @GET("users/{id}/presence")
    fun getUserStatus(@Path("id") id: Int): Single<StatusUserResponse>

    @GET("users/me?json=true")
    fun getOwnUser(): Single<OwnUser>

    @POST("messages/{message_id}/reactions")
    @FormUrlEncoded
    fun addReaction(
        @Path("message_id") message_id: Int,
        @Field("emoji_name") emoji_name: String,
        @Field("reaction_type") reaction_type: String
    ): Call<AddReactionResponse>

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") message_id: Int,
        @Query("emoji_name") emoji_name: String,
        @Query("reaction_type") reaction_type: String
    ): Call<AddReactionResponse>

    @POST("messages")
    @FormUrlEncoded
    fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String,
        @Field("topic") topic: String
    ): Call<SendMessageResponse>

    @POST("messages/flags")
    @FormUrlEncoded
    fun updateMessageFlag(
        @Field("messages") messages: String,
        @Field("op") op: String,
        @Field("flag") flag: String
    ): Call<UpdateMessageFlag>

}
