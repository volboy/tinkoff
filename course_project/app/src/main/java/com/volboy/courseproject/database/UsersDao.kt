package com.volboy.courseproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volboy.courseproject.model.UserJSON
import io.reactivex.Maybe

@Dao
interface UsersDao {
    @Query("SELECT * FROM UserJSON")
    fun getAllUsers():Maybe<List<UserJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUsers(userJSON: UserJSON)
}