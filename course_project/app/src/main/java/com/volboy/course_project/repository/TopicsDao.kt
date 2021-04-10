package com.volboy.course_project.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.volboy.course_project.model.TopicJSON

@Dao
interface TopicsDao {
    @Query("SELECT * FROM TopicJSON")
    fun getAllTopics(): List<TopicJSON>

    @Update
    fun updateTopics(topicJSON: TopicJSON)
}