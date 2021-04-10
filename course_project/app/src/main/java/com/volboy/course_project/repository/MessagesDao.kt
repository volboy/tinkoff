package com.volboy.course_project.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.volboy.course_project.model.MessageJSON
import io.reactivex.Maybe

@Dao
interface MessagesDao {
    @Query("SELECT * FROM MessageJSON")
    fun getAllMessages(): Maybe<List<MessageJSON>>

    @Update
    fun updateMessages(messageJSON: MessageJSON)
}