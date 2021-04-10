package com.volboy.course_project.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.volboy.course_project.model.StreamJSON
import io.reactivex.Maybe

@Dao
    interface StreamsDao {
        @Query("SELECT * FROM StreamJSON")
        fun getAllStreams(): Maybe<List<StreamJSON>>

        @Update
        fun updateStreams(streamJSON: StreamJSON)
    }
