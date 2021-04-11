package com.volboy.course_project.repository

import androidx.room.*
import com.volboy.course_project.model.TopicJSON

@Dao
interface TopicsDao {
    @Query("SELECT * FROM TopicJSON")
    fun getAllTopics(): List<TopicJSON>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateTopics(topicJSON: TopicJSON)
}