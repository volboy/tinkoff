package com.volboy.courseproject.recyclerview

import androidx.recyclerview.widget.DiffUtil

class CommonDiffUtilCallback : DiffUtil.ItemCallback<ViewTyped>() {
    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem.uid == newItem.uid
    }
}
