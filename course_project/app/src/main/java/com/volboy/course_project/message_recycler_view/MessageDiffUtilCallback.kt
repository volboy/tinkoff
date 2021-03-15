package com.volboy.course_project.message_recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.volboy.course_project.model.Message

class MessageDiffUtilCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun getChangePayload(oldItem: Message, newItem: Message): Any? {
        return super.getChangePayload(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id //пока так, нужно переопределить equals
    }

}