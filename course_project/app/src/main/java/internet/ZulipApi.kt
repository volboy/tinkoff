package internet

import com.volboy.course_project.model.SendedMessage
import com.volboy.course_project.model.StreamResponse
import com.volboy.course_project.model.TopicResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ZulipApi {
    @GET("streams?json=true")
    fun getStreams(): Single<StreamResponse>

    @GET("users/me/{id}/topics?json=true")
    fun getStreamsTopics(@Path("id") id: Int): Single<TopicResponse>

    @POST("messages")
    @FormUrlEncoded
    fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String,
        @Field("topic") topic: String
    ): Call<SendedMessage>
}