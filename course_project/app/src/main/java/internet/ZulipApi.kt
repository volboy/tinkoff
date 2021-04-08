package internet

import com.volboy.course_project.model.*
import io.reactivex.Single
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

    @GET("users?json=true")
    fun getUsers(): Single<UsersResponse>

    @GET("users/me?json=true")
    fun getOwnUser(): Single<OwnUser>
}
