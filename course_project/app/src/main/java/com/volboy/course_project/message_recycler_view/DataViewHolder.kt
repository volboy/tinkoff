package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R

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