package com.volboy.courseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.courseproject.model.SubStreamJSON
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface SubStreamsDao {
    @Query("SELECT * FROM SubStreamJSON")
    fun getSubStreams(): Maybe<List<SubStreamJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateSubStreams(subStreamJSON: List<SubStreamJSON>): Completable
}