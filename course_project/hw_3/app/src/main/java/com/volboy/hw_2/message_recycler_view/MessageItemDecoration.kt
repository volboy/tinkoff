package com.volboy.hw_2.message_recycler_view


import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.volboy.hw_2.R
import com.volboy.hw_2.customviews.dpToPx
import com.volboy.hw_2.customviews.spToPx

class MessageItemDecoration(var divider: String, context: Context) : RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint().apply {
        color = context.resources.getColor(R.color.data_divider_text_color)
        textAlign = Paint.Align.CENTER
        textSize = context.spToPx(14F).toFloat()
    }
    private val mRoundRectPaint = Paint().apply {
        color = context.resources.getColor(R.color.data_divider_color)
        style = Paint.Style.FILL_AND_STROKE
    }
    val radiusRect = context.dpToPx(58F).toFloat()
    private var mRoundRectPoint = RectF()
    private var dividerWidth = (context.dpToPx(76F)).toFloat()
    private var dividerHeight = (context.dpToPx(22F)).toFloat()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = 66

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val canvasCount = c.save()
        parent.children
            .forEach { children ->
                mRoundRectPoint.set(
                    parent.width / 2F - dividerWidth / 2,
                    children.top.toFloat() - 11 - dividerHeight,
                    parent.width / 2F + dividerWidth / 2,
                    children.top.toFloat() - 11
                )
                c.drawRoundRect(mRoundRectPoint, radiusRect, radiusRect, mRoundRectPaint)
                c.drawText(divider, parent.width / 2F, children.top.toFloat() - 33, dividerPaint)
            }
        c.restoreToCount(canvasCount)
    }
}