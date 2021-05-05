package com.volboy.courseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.courseproject.model.TopicForDB
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface TopicsDao {
    @Query("SELECT * FROM TopicForDB WHERE streamId=:id")
    fun getTopicsOfStream(id: Int): Maybe<List<TopicForDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateTopics(topicsForDB: List<TopicForDB>): Completable
}