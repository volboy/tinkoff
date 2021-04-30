package com.volboy.courseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.courseproject.model.MessageJSON
import io.reactivex.Maybe

@Dao
interface MessagesDao {
    @Query("SELECT * FROM MessageJSON")
    fun getAllMessages(): Maybe<List<MessageJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateMessages(messagesJSON: List<MessageJSON>)
}