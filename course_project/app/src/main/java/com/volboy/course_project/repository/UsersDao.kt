package com.volboy.course_project.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.volboy.course_project.model.UserJSON

@Dao
interface UsersDao {
    @Query("SELECT * FROM UserJSON")
    fun getAllUsers():List<UserJSON>

    @Update
    fun updateUsers(userJSON: UserJSON)
}