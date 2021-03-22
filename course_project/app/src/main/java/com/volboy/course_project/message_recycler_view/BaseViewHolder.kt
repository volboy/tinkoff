package com.volboy.course_project.message_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewTyped>(private val containerView: View) : RecyclerView.ViewHolder(containerView) {

    open fun bind(item: T) = Unit

    open fun bind(item: T, payload: List<Any>) = Unit

}