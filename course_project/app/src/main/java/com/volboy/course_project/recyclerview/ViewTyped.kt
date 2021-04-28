package com.volboy.course_project.recyclerview

interface ViewTyped {
    val viewType: Int
        get() = error("provide viewType $this")
    val uid: String
        get() = error("provide uid for viewType $this")
}