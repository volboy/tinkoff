package com.volboy.hw_2.customviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
        color = getColor(resources, R.color.ev_text_color, null)
        textAlign = Paint.Align.CENTER

    }


    private val mRoundRectPaint = Paint().apply {
        color = getColor(resources, R.color.ev_unselected, null)
        style = Paint.Style.FILL_AND_STROKE
    }


    private var textSize: Int
        get() = emojiPaint.textSize.toInt()
        set(value) {
            if (emojiPaint.textSize.toInt() != value) {
                emojiPaint.textSize = value.toFloat()
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
    private var mRoundRectPoint = RectF()
    private var radiusRect = 0.0f
    private val emojiBounds = Rect()


    init {
        context.obtainStyledAttributes(attrs, R.styleable.EmojiView).apply {
            textSize = getDimensionPixelSize(
                R.styleable.EmojiView_ev_text_size, context.spToPx(
                    14F
                )
            )


            foregroundDrawable = getDrawable(R.styleable.EmojiView_ev_foreground)

            var emojiCode = getInteger(R.styleable.EmojiView_ev_emoji, 0)

            text = getText(R.styleable.EmojiView_ev_text)?.toString() ?: "0"
            if (text == "") text = "0"
            emoji = String(Character.toChars(emojiCode)) + " " + text
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.getTextBounds(emoji, 0, emoji.length, emojiBounds)
        val emojiWidth = emojiBounds.width()
        val emojiHeight = emojiBounds.height()
        val mRoundRectWidth = emojiWidth + paddingLeft + paddingRight + context.dpToPx(18.0f)
        val mRoundRectHeight = emojiHeight + paddingBottom + paddingTop + context.dpToPx(9.6f)
        var contentWidth = mRoundRectWidth
        var contentHeight = mRoundRectHeight

        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        contentWidth = when (mode) {
            MeasureSpec.EXACTLY -> widthMeasureSpec
            MeasureSpec.AT_MOST -> contentWidth
            else -> contentWidth

        }
        contentHeight = when (mode) {
            MeasureSpec.EXACTLY -> heightMeasureSpec
            MeasureSpec.AT_MOST -> contentHeight
            else -> contentHeight

        }
        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val x = (width / 2).toFloat()
        val y = height / 2f - emojiBounds.centerY()
        emojiPoint.set(x, y)

        mRoundRectPoint.set(
            0f,
            height.toFloat(),
            width.toFloat(),
            0f
        )
        radiusRect = context.dpToPx(10.0f).toFloat()


    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        canvas.drawRoundRect(mRoundRectPoint, radiusRect, radiusRect, mRoundRectPaint)
        canvas.drawText(emoji, emojiPoint.x, emojiPoint.y, emojiPaint)
        canvas.restoreToCount(canvasCount)
    }

    /* override fun onCreateDrawableState(extraSpace: Int): IntArray {
         val drawableState = super.onCreateDrawableState(extraSpace + 1)

         if (isSelected) {
             mergeDrawableStates(drawableState, DRAWABLES_STATE)
         }

         return drawableState
     }*/

    override fun performClick(): Boolean {
        isSelected = !isSelected
        if (isSelected) mRoundRectPaint.color = getColor(resources, R.color.ev_selected, null) else
            mRoundRectPaint.color = getColor(resources, R.color.ev_unselected, null)
        return super.performClick()
    }

    companion object {
        private val DRAWABLES_STATE = IntArray(1) { android.R.attr.state_selected }
    }

}

