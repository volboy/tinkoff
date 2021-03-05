package com.volboy.hw_2.CustomViews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import com.volboy.hw_2.R


class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val emojiPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.RIGHT
    }

    private val mRoundRectPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE
    }


    private var emojiSize: Int
        get() = emojiPaint.textSize.toInt()
        set(value) {
            if (emojiPaint.textSize.toInt() != value) {
                emojiPaint.textSize = value.toFloat()
                requestLayout()
            }
        }
    private var textSize: Int
        get() = textPaint.textSize.toInt()
        set(value) {
            if (textPaint.textSize.toInt() != value) {
                textPaint.textSize = value.toFloat()
                requestLayout()
            }
        }
    private var emoji: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    private var text: String = "0"
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    private var foregroundDrawable: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    private var emojiPoint = PointF()
    private var textPoint = PointF()
    private var mRoundRectPoint = RectF()
    private var radiusRect = 0.0f
    private val emojiBounds = Rect()
    private val textBounds = Rect()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.EmojiView).apply {
            emojiSize = getDimensionPixelSize(
                R.styleable.EmojiView_ev_text_size, context.spToPx(
                    14F
                )
            )
            textSize = getDimensionPixelSize(
                R.styleable.EmojiView_ev_text_size, context.spToPx(
                    14F
                )

            )

            foregroundDrawable = getDrawable(R.styleable.EmojiView_ev_foreground)

            var emojiCode = getInteger(R.styleable.EmojiView_ev_emoji, 0)
            emoji = String(Character.toChars(emojiCode))
            text = getText(R.styleable.EmojiView_ev_text)?.toString() ?: "0"
            if (text == "") text = "0"
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.getTextBounds(emoji, 0, emoji.length, emojiBounds)
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val emojiWidth = emojiBounds.width()
        val emojiHeight = emojiBounds.height()
        val textWidth = textBounds.width()
        val mRoundRectWidth = emojiWidth + textWidth + textSize / 4
        val mRoundRectHeight = emojiHeight + textSize / 4
        var contentWidth = mRoundRectWidth + 10 + paddingLeft + paddingRight
        val contentHeight = mRoundRectHeight + 10

        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        contentWidth = when (mode) {
            MeasureSpec.EXACTLY -> widthMeasureSpec
            MeasureSpec.AT_MOST -> contentWidth
            else -> contentWidth

        }
        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val yForText = bottom.toFloat() - (height - textBounds.height()) / 2f
        emojiPoint.set(
            left.toFloat() + paddingLeft,
            yForText
        )
        textPoint.set(
            right.toFloat() - paddingRight,
            yForText
        )
        mRoundRectPoint.set(
            left.toFloat() + 5,
            top.toFloat() + 5,
            right.toFloat() - 5,
            bottom.toFloat() - 5
        )
        radiusRect = width * 0.15f


    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        canvas.drawRoundRect(mRoundRectPoint, radiusRect, radiusRect, mRoundRectPaint)
        canvas.drawText(emoji, emojiPoint.x, emojiPoint.y, emojiPaint)
        canvas.drawText(text, textPoint.x, textPoint.y, textPaint)
        canvas.restoreToCount(canvasCount)
    }

    /*override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)

        if (isSelected) {
            mergeDrawableStates(drawableState, DRAWABLES_STATE)
        }

        return drawableState
    }*/

    override fun performClick(): Boolean {
        isSelected = !isSelected
        if (isSelected) mRoundRectPaint.color= getColor(resources, R.color.ev_selected,null ) else
            mRoundRectPaint.color= getColor(resources, R.color.ev_unselected,null )
        return super.performClick()
    }

    companion object {
        private val DRAWABLES_STATE = IntArray(1) { android.R.attr.state_selected }
    }

}

