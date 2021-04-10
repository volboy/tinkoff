package com.volboy.course_project.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.volboy.course_project.model.MessageJSON
import com.volboy.course_project.model.StreamJSON
import com.volboy.course_project.model.TopicJSON
import com.volboy.course_project.model.UserJSON


@Database(entities = [MessageJSON::class, StreamJSON::class, TopicJSON::class, UserJSON::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun streamsDao(): StreamsDao
    abstract fun topicsDao(): TopicsDao
    abstract fun usersDao(): UsersDao
}