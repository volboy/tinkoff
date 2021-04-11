package com.volboy.course_project.repository

import androidx.room.*
import com.volboy.course_project.model.MessageJSON
import io.reactivex.Maybe

@Dao
interface MessagesDao {
    @Query("SELECT * FROM MessageJSON")
    fun getAllMessages(): Maybe<List<MessageJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateMessages(messageJSON: MessageJSON)
}