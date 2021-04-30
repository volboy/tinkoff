package com.volboy.courseproject.presentation.streams

import android.content.Context
import android.graphics.Canvas
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.volboy.courseproject.R

class StreamsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val colorOne = context.resources.getColor(R.color.expand_item_color_one)
    private val colorTwo = context.resources.getColor(R.color.expand_item_color_two)
    private var viewType: Int? = null
    private var position = 0

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        parent.children.forEach { child ->
            position = parent.getChildLayoutPosition(child)
            if (position >= 0) {
                viewType = adapter?.getItemViewType(parent.getChildLayoutPosition(child))
                if (viewType == R.layout.item_expand && parent.getChildLayoutPosition(child) % 2 == 0) {
                    child.setBackgroundColor(colorOne)
                } else if (viewType == R.layout.item_expand) {
                    child.setBackgroundColor(colorTwo)
                }
            }
        }
    }
}