package com.volboy.courseproject.presentation.messages

import android.view.View
import android.widget.TextView
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.ItemDateDividerBinding
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

class DataUi(
    val data: String,
    override val viewType: Int = R.layout.item_date_divider,
    override val uid: String = "date"
) : ViewTyped

class DataViewHolder(view: View) : BaseViewHolder<DataUi>(view) {
    private val itemDateDividerBinding = ItemDateDividerBinding.bind(view)
    private val data: TextView = itemDateDividerBinding.date

    override fun bind(item: DataUi) {
        data.text = item.data
    }
}