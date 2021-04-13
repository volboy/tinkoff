package com.volboy.course_project.repository

import androidx.room.*
import com.volboy.course_project.model.UserJSON
import io.reactivex.Maybe

@Dao
interface UsersDao {
    @Query("SELECT * FROM UserJSON")
    fun getAllUsers():Maybe<List<UserJSON>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUsers(userJSON: UserJSON)
}