package com.volboy.hw_2.customviews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.volboy.hw_2.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val childrenRect = Rect()
    private val lastEmojiRect = Rect()
    private lateinit var lastEmojiView: EmojiView

    init {
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var currentWidth = 0
        var currentHeight = 0
        var resultWidth = 0
        var resultHeight = 0
        var countRows = 0
        var lastEmojiViewWidth=0
        var lastEmojiViewHeight=0

        children.forEach { children ->
            measureChildWithMargins(children, widthMeasureSpec, currentWidth, heightMeasureSpec, resultHeight)
            var childrenLayoutParams = children.layoutParams as MarginLayoutParams
            currentWidth += children.measuredWidth + childrenLayoutParams.leftMargin + childrenLayoutParams.rightMargin
            currentHeight = children.measuredHeight + childrenLayoutParams.topMargin + childrenLayoutParams.bottomMargin
            if (currentWidth >= width) {
                resultWidth = currentWidth
                currentWidth = 0
                countRows++
                resultHeight = currentHeight * countRows
            }
        }
        LayoutInflater.from(context).inflate(R.layout.last_emoji_view, this, true)
        lastEmojiView=findViewById(R.id.lastEmojiView)
        val lastEmojiLayoutParams=lastEmojiView.layoutParams as MarginLayoutParams
        measureChildWithMargins(lastEmojiView, widthMeasureSpec,resultWidth,heightMeasureSpec,resultHeight)
        lastEmojiViewWidth=lastEmojiView.measuredWidth + lastEmojiLayoutParams.leftMargin + lastEmojiLayoutParams.rightMargin
        lastEmojiViewHeight=lastEmojiView.measuredHeight + lastEmojiLayoutParams.topMargin + lastEmojiLayoutParams.bottomMargin
        if (resultWidth+lastEmojiViewWidth>=width){
            resultHeight+=lastEmojiViewHeight
        }

        setMeasuredDimension(
            resolveSize(resultWidth, widthMeasureSpec),
            resolveSize(resultHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentWidth = 0
        var currentHeight = 0
        children.forEach { children ->
            var childrenLayoutParams = children.layoutParams as MarginLayoutParams
            childrenRect.left = childrenLayoutParams.leftMargin + currentWidth
            childrenRect.top = childrenLayoutParams.topMargin + currentHeight
            childrenRect.right = childrenRect.left + children.measuredWidth
            childrenRect.bottom = childrenRect.top + children.measuredHeight
            currentWidth += childrenRect.width() + childrenLayoutParams.rightMargin
            if (currentWidth > width - childrenRect.width()) {
                currentWidth = 0
                currentHeight += childrenRect.height() + childrenLayoutParams.bottomMargin
            }
            children.layout(childrenRect)
        }

    }

    override fun generateDefaultLayoutParams(): LayoutParams = MarginLayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    private fun View.layout(rect: Rect) {
        layout(rect.left, rect.top, rect.right, rect.bottom)
    }
}