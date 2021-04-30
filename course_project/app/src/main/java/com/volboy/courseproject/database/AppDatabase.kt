package com.volboy.courseproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.volboy.courseproject.model.MessageJSON
import com.volboy.courseproject.model.StreamJSON
import com.volboy.courseproject.model.TopicJSON
import com.volboy.courseproject.model.UserJSON

@Database(entities = [MessageJSON::class, StreamJSON::class, TopicJSON::class, UserJSON::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun streamsDao(): StreamsDao
    abstract fun topicsDao(): TopicsDao
    abstract fun usersDao(): UsersDao
}