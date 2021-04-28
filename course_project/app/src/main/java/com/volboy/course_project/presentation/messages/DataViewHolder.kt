package com.volboy.course_project.presentation.messages

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.recyclerview.BaseViewHolder
import com.volboy.course_project.recyclerview.ViewTyped

class DataUi(
    val data: String,
    override val viewType: Int = R.layout.item_date_divider,
    override val uid: String = ""
) : ViewTyped

class DataViewHolder(view: View) : BaseViewHolder<DataUi>(view) {
    private val data: TextView = view.findViewById(R.id.date)

    override fun bind(item: DataUi) {
        data.text = item.data
    }
}