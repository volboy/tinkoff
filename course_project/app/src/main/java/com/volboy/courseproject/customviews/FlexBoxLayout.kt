package com.volboy.courseproject.customviews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.volboy.courseproject.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val childrenRect = Rect()
    private var parentWidth: Int = 0
    private lateinit var layoutParams: MarginLayoutParams

    init {
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var currentWidth = 0
        var currentHeight: Int
        var resultWidth = 0
        var resultHeight = 0
        var countRows = 1
        parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, currentWidth, heightMeasureSpec, resultHeight)
            val childrenLayoutParams = child.layoutParams as MarginLayoutParams
            currentWidth += child.measuredWidth + childrenLayoutParams.marginStart + childrenLayoutParams.marginEnd
            currentHeight = child.measuredHeight + childrenLayoutParams.topMargin + childrenLayoutParams.bottomMargin
            if (currentWidth >= parentWidth - child.measuredWidth + context.dpToPx(DEFAULT_MARGIN_END_EMOJI)) {
                resultWidth = currentWidth
                currentWidth = 0
                countRows++
                resultHeight = currentHeight * countRows
            }
            if (countRows == 1) {
                resultHeight = currentHeight
            }
        }
        setMeasuredDimension(resolveSize(resultWidth, widthMeasureSpec), resolveSize(resultHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentWidth = 0
        var currentHeight = 0
        children.forEach { child ->
            val childrenLayoutParams = child.layoutParams as MarginLayoutParams
            childrenRect.left = childrenLayoutParams.marginStart + currentWidth
            childrenRect.top = childrenLayoutParams.topMargin + currentHeight
            childrenRect.right = childrenRect.left + child.measuredWidth
            childrenRect.bottom = childrenRect.top + child.measuredHeight
            currentWidth += childrenRect.width() + childrenLayoutParams.marginEnd
            if (currentWidth > parentWidth - childrenRect.width()) {
                currentWidth = 0
                currentHeight += childrenRect.height() + childrenLayoutParams.bottomMargin
            }
            child.layout(childrenRect)
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, context.dpToPx(DEFAULT_HEIGHT))
        layoutParams.marginStart = context.dpToPx(DEFAULT_START_MARGIN)
        layoutParams.marginEnd = context.dpToPx(DEFAULT_END_MARGIN)
        layoutParams.bottomMargin = context.dpToPx(DEFAULT_BOTTOM_MARGIN)
        return layoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    private fun View.layout(rect: Rect) {
        layout(rect.left, rect.top, rect.right, rect.bottom)
    }

    override fun addView(child: View?) {
        super.addView(child)
        child?.setBackgroundResource(R.drawable.emoji_view_state)

    }

    fun addLastView(emojiView: EmojiView) {
        layoutParams = MarginLayoutParams(context.dpToPx(DEFAULT_WIDTH), context.dpToPx(DEFAULT_HEIGHT))
        layoutParams.marginStart = context.dpToPx(DEFAULT_START_MARGIN)
        layoutParams.marginEnd = context.dpToPx(DEFAULT_END_MARGIN)
        layoutParams.bottomMargin = context.dpToPx(DEFAULT_BOTTOM_MARGIN)
        emojiView.layoutParams = layoutParams
        this.addView(emojiView)
    }

    private companion object {
        const val DEFAULT_START_MARGIN = 10F
        const val DEFAULT_END_MARGIN = 7F
        const val DEFAULT_BOTTOM_MARGIN = 7F
        const val DEFAULT_WIDTH = 45F
        const val DEFAULT_HEIGHT = 30F
        const val DEFAULT_MARGIN_END_EMOJI = 20F
    }
}
