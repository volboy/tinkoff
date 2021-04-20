package com.volboy.course_project.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.course_project.model.TopicJSON
import io.reactivex.Maybe

@Dao
interface TopicsDao {
    @Query("SELECT * FROM TopicJSON")
    fun getAllTopics(): Maybe<List<TopicJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateTopics(topicsJSON: List<TopicJSON>)
}