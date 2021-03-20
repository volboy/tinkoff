package com.volboy.course_project.model

class Reaction(val emoji: String, val usersId: String, var count: Int) {
    override fun toString(): String {
        return "Reaction(emoji='$emoji', usersId='$usersId', count=$count)"
    }

}