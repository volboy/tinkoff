package com.volboy.courseproject.api

import com.volboy.courseproject.model.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ZulipApi {
    @GET("streams?json=true")
    fun getStreams(): Single<StreamResponse>

    @GET("users/me/subscriptions?json=true")
    fun getSubscribedStreams(): Single<SubStreamResponse>

    @GET("users/me/{id}/topics?json=true")
    fun getStreamsTopics(@Path("id") id: Int): Single<TopicResponse>

    @GET("messages?json=true")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String
    ): Single<MessageResponse>

    @GET("messages?json=true")
    fun getMessagesNext(
        @Query("anchor") anchor: Int,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
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
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
        @Field("reaction_type") reactionType: String
    ): Single<AddReactionResponse>

    @POST("users/me/subscriptions")
    @FormUrlEncoded
    fun subscribeToStream(
        @Field("subscriptions") subscriptions: String,
        @Field("invite_only") inviteOnly: Boolean
    ): Single<SubscribedJSON>

    @POST("messages")
    @FormUrlEncoded
    fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String,
        @Field("topic") topic: String
    ): Single<SendMessageResponse>

    @POST("messages/flags")
    @FormUrlEncoded
    fun updateMessageFlag(
        @Field("messages") messages: String,
        @Field("op") op: String,
        @Field("flag") flag: String
    ): Call<UpdateMessageFlag>

    @DELETE("users/me/subscriptions")
    fun unSubscribeToStream(
        @Query("subscriptions") subscriptions: String,
    ): Single<SubscribedJSON>

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("reaction_type") reactionType: String
    ): Single<AddReactionResponse>

    @DELETE("messages/{message_id}")
    fun deleteMessage(
        @Path("message_id") messageId: Int
    ): Single<DeleteMessageResponse>

    @PATCH("messages/{message_id}")
    fun editMessage(
        @Path("message_id") messageId: Int,
        @Query("content") content: String,
    ): Single<DeleteMessageResponse>

    @PATCH("messages/{message_id}")
    fun changeTopicOfMessage(
        @Path("message_id") messageId: Int,
        @Query("topic") topic: String
    ): Single<DeleteMessageResponse>

}
