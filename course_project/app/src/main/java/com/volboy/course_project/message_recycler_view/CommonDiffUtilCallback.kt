package com.volboy.course_project.message_recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.volboy.course_project.model.Message

class CommonDiffUtilCallback : DiffUtil.ItemCallback<ViewTyped>() {
    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem.uid == newItem.uid
    }
}