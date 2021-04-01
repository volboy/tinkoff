package internet

import com.volboy.course_project.model.StreamJSON
import retrofit2.Call
import retrofit2.http.GET

interface ZulipApi {
    @GET("streams?json=true")
    fun getStreams(): Call<StreamJSON>

/*    @GET("0?json=true")
    fun getPosts():Call<Stream>

    @GET("hot/{id}")
    fun getPostById(@Path("id") int id):Call<Stream>*/
}