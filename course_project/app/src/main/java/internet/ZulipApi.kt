package internet

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
        @QueryName(encoded = true) narrow: Array<Map<String, String>>
    ): Single<MessageResponse>
}
