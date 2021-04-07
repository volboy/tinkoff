package com.volboy.course_project.message_recycler_view.simple_items

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped

object ProgressItem : ViewTyped {
    override val uid: String = "PROGRESS_ITEM_ID"
    override val viewType: Int = R.layout.item_progress
}

object ErrorItem : ViewTyped {
    override val uid: String = "ERROR_ITEM_ID"
    override val viewType: Int = R.layout.item_error
}