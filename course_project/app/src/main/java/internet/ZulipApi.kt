package internet

import com.volboy.course_project.model.SendedMessage
import com.volboy.course_project.model.StreamResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ZulipApi {
    @GET("streams?json=true")
    fun getStreams(): Single<StreamResponse>

    @POST("messages")
    @FormUrlEncoded
    fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String,
        @Field("topic") topic: String
    ): Call<SendedMessage>

/*    @GET("0?json=true")
    fun getPosts():Call<Stream>

    @GET("hot/{id}")
    fun getPostById(@Path("id") int id):Call<Stream>*/
}