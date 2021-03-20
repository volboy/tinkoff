package com.volboy.course_project.customviews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.volboy.course_project.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val childrenRect = Rect()
    private var lastEmojiView: EmojiView

    init {
        setWillNotDraw(true)
        LayoutInflater.from(context).inflate(R.layout.last_emoji_view, this, true)
        lastEmojiView = findViewById(R.id.lastEmojiView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var currentWidth = 0
        var currentHeight = 0
        var resultWidth = 0
        var resultHeight = 0
        var countRows = 1
        var countChildren = 0
        val lastEmojiViewWidth: Int
        val lastEmojiViewHeight: Int
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        children.forEach { child ->
            if (child != lastEmojiView) {
                measureChildWithMargins(child, widthMeasureSpec, currentWidth, heightMeasureSpec, resultHeight)
                val childrenLayoutParams = child.layoutParams as MarginLayoutParams
                currentWidth += child.measuredWidth + childrenLayoutParams.leftMargin + childrenLayoutParams.rightMargin
                currentHeight = child.measuredHeight + childrenLayoutParams.topMargin + childrenLayoutParams.bottomMargin
                if (currentWidth >= parentWidth - child.measuredWidth) {
                    resultWidth = currentWidth
                    currentWidth = 0
                    countRows++
                    resultHeight = currentHeight * countRows
                }
            }
            if (countRows == 1) {
                resultHeight = currentHeight
            }
        }

        children.forEach { _ -> countChildren++ }
        if (countChildren != 0) {
            val lastEmojiLayoutParams = lastEmojiView.layoutParams as MarginLayoutParams
            measureChildWithMargins(lastEmojiView, widthMeasureSpec, resultWidth, heightMeasureSpec, resultHeight)
            lastEmojiViewWidth = lastEmojiView.measuredWidth + lastEmojiLayoutParams.leftMargin + lastEmojiLayoutParams.rightMargin
            lastEmojiViewHeight = lastEmojiView.measuredHeight + lastEmojiLayoutParams.topMargin + lastEmojiLayoutParams.bottomMargin
            if (resultWidth + lastEmojiViewWidth >= parentWidth) {
                resultHeight += lastEmojiViewHeight
            }
        }
        setMeasuredDimension(resolveSize(resultWidth, widthMeasureSpec), resolveSize(resultHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentWidth = 0
        var currentHeight = 0
        children.forEach { child ->
            if (child != lastEmojiView) {
                val childrenLayoutParams = child.layoutParams as MarginLayoutParams
                childrenRect.left = childrenLayoutParams.leftMargin + currentWidth
                childrenRect.top = childrenLayoutParams.topMargin + currentHeight
                childrenRect.right = childrenRect.left + child.measuredWidth
                childrenRect.bottom = childrenRect.top + child.measuredHeight
                currentWidth += childrenRect.width() + childrenLayoutParams.rightMargin
                if (currentWidth > width - childrenRect.width()) {
                    currentWidth = 0
                    currentHeight += childrenRect.height() + childrenLayoutParams.bottomMargin
                }
                child.layout(childrenRect)
            }
        }
        var lastChild = children.firstOrNull { child -> child == lastEmojiView }
        if (lastChild != null) {
            val childrenLayoutParams = lastChild.layoutParams as MarginLayoutParams
            childrenRect.left = childrenLayoutParams.leftMargin + currentWidth
            childrenRect.top = childrenLayoutParams.topMargin + currentHeight
            childrenRect.right = childrenRect.left + lastChild.measuredWidth
            childrenRect.bottom = childrenRect.top + lastChild.measuredHeight
            lastChild.layout(childrenRect)
        }

    }

    override fun generateDefaultLayoutParams(): LayoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    private fun View.layout(rect: Rect) {
        layout(rect.left, rect.top, rect.right, rect.bottom)
    }
}