package com.volboy.course_project.repository

import androidx.room.*
import com.volboy.course_project.model.UserJSON

@Dao
interface UsersDao {
    @Query("SELECT * FROM UserJSON")
    fun getAllUsers():List<UserJSON>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUsers(userJSON: UserJSON)
}