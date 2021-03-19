package com.volboy.course_project.customviews


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import com.volboy.course_project.R


class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var resultText = ""
    private var emojiPoint = PointF()
    private var mRoundRectPoint = RectF()
    private var radiusRect = 0.0f
    private val emojiBounds = Rect()
    private val emojiPaint = Paint().apply {
        color = getColor(resources, R.color.ev_text_color, null)
        textAlign = Paint.Align.CENTER
    }
    private val mRoundRectPaint = Paint().apply {
        color = getColor(resources, R.color.ev_unselected, null)
        style = Paint.Style.STROKE
    }
    private var textSize: Int
        get() = emojiPaint.textSize.toInt()
        set(value) {
            if (emojiPaint.textSize.toInt() != value) {
                emojiPaint.textSize = value.toFloat()
                requestLayout()
            }
        }
    var emoji: String = DEFAULT_EMOJI_CODE.toString()
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var text: String = DEFAULT_TEXT
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.EmojiView).apply {
            textSize = getDimensionPixelSize(R.styleable.EmojiView_ev_text_size, context.spToPx(DEFAULT_FONT_SIZE_PX))
            var emojiCode = getInteger(R.styleable.EmojiView_ev_emoji, DEFAULT_EMOJI_CODE)
            text = getText(R.styleable.EmojiView_ev_text)?.toString() ?: DEFAULT_TEXT
            if (text == "") text = DEFAULT_TEXT
            emoji = String(Character.toChars(emojiCode))
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        resultText = if (emoji == String(Character.toChars(0))) {
            text
        } else {
            "$emoji $text"
        }
        emojiPaint.getTextBounds(resultText, 0, resultText.length, emojiBounds)
        val emojiWidth = emojiBounds.width()
        val emojiHeight = emojiBounds.height()
        val mRoundRectWidth = emojiWidth + paddingLeft + paddingRight + context.dpToPx(PADDINGS_LEFT_AND_RIGHT)
        val mRoundRectHeight = emojiHeight + paddingBottom + paddingTop + context.dpToPx(PADDINGS_TOP_AND_BOTTOM)
        var contentWidth = mRoundRectWidth
        var contentHeight = mRoundRectHeight
        var mode = MeasureSpec.getMode(widthMeasureSpec)
        contentWidth = when (mode) {
            MeasureSpec.EXACTLY -> widthMeasureSpec
            MeasureSpec.AT_MOST -> contentWidth
            else -> contentWidth
        }
        mode = MeasureSpec.getMode(heightMeasureSpec)
        contentHeight = when (mode) {
            MeasureSpec.EXACTLY -> heightMeasureSpec
            MeasureSpec.AT_MOST -> contentHeight
            else -> contentHeight
        }
        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val x = width / 2F
        val y = height / 2F - emojiBounds.centerY()
        emojiPoint.set(x, y)
        mRoundRectPoint.set(0F, height.toFloat(), width.toFloat(), 0F)
        radiusRect = context.dpToPx(RADIUS_RECT).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        canvas.drawRoundRect(mRoundRectPoint, radiusRect, radiusRect, mRoundRectPaint)
        canvas.drawText(resultText, emojiPoint.x, emojiPoint.y, emojiPaint)
        canvas.restoreToCount(canvasCount)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isSelected) {
            mergeDrawableStates(drawableState, DRAWABLES_STATE)
        }
        return drawableState
    }

    companion object {
        private const val DEFAULT_FONT_SIZE_PX = 14F
        private const val DEFAULT_EMOJI_CODE = 0
        private const val DEFAULT_TEXT = ""
        private const val RADIUS_RECT = 10.0F
        private const val PADDINGS_LEFT_AND_RIGHT = 18.0F
        private const val PADDINGS_TOP_AND_BOTTOM = 9.6F
        private val DRAWABLES_STATE = IntArray(1) { android.R.attr.state_selected }
    }
}

