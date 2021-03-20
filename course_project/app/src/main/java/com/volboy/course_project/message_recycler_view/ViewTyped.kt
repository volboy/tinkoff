package com.volboy.course_project.message_recycler_view

interface ViewTyped {
    val viewType: Int
        get() = error("provide viewType $this")
    val uid: String
        get() = error("provide uid for viewType $this")
}