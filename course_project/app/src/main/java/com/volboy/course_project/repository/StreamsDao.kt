package com.volboy.course_project.repository

import androidx.room.*
import com.volboy.course_project.model.StreamJSON
import io.reactivex.Maybe

@Dao
interface StreamsDao {
    @Query("SELECT * FROM StreamJSON")
    fun getAllStreams(): Maybe<List<StreamJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateStreams(streamsJSON: List<StreamJSON>)
}
