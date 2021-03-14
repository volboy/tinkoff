package com.volboy.hw_2.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.hw_2.R

class DataUi(val data: String, override val viewType: Int = R.layout.date_divider_item) : ViewTyped

class DataViewHolder(view: View) : BaseViewHolder<DataUi>(view) {
    private val data: TextView = view.findViewById(R.id.date)

    override fun bind(item: DataUi) {
        data.text = item.data
    }
}