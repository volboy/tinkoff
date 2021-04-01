package internet

import com.volboy.course_project.model.Stream
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ZulipService {
    @GET("streams?json=true")
    fun getPost(): Call<Stream>

/*    @GET("0?json=true")
    fun getPosts():Call<Stream>

    @GET("hot/{id}")
    fun getPostById(@Path("id") int id):Call<Stream>*/
}