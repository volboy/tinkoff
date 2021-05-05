package com.volboy.courseproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.volboy.courseproject.model.*

@Database(entities = [MessageJSON::class, StreamJSON::class, TopicForDB::class, UserJSON::class, SubStreamJSON::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun streamsDao(): StreamsDao
    abstract fun topicsDao(): TopicsDao
    abstract fun usersDao(): UsersDao
    abstract fun subStreamsDao(): SubStreamsDao
}