package com.volboy.courseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.courseproject.model.StreamJSON
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface StreamsDao {
    @Query("SELECT * FROM StreamJSON")
    fun getAllStreams(): Maybe<List<StreamJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateStreams(streamsJSON: List<StreamJSON>): Completable
}
