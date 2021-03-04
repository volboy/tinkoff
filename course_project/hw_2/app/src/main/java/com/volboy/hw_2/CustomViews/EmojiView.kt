package com.volboy.hw_2.CustomViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.volboy.hw_2.R


class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val emojiPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.RIGHT
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.RIGHT
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
    private var emojiPoint = PointF()
    private var textPoint = PointF()
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
            var emojiCode = getInteger(R.styleable.EmojiView_ev_emoji, 0)
            emoji = String(Character.toChars(emojiCode))
            text = getText(R.styleable.EmojiView_ev_text)?.toString() ?: "0"
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.getTextBounds(emoji, 0, emoji.length, emojiBounds)
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val emojiWidth = emojiBounds.width()
        val emojiHeight = emojiBounds.height()
        val textWidth = textBounds.width()
        var contentWidth = 100/*emojiWidth + textWidth + paddingRight +paddingLeft*/
        val contentHeight = emojiHeight + paddingTop + paddingBottom

        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        contentWidth = when (mode) {
            MeasureSpec.EXACTLY -> contentWidth
            MeasureSpec.AT_MOST -> contentWidth
            else -> contentWidth

        }
        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val emptySpaceVertical=(height-textBounds.height())/2f
        emojiPoint.set(emojiBounds.right.toFloat(), emptySpaceVertical+textBounds.height())
        textPoint.set(emojiBounds.width() + textBounds.width().toFloat()*2, emptySpaceVertical+textBounds.height())

    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        canvas.drawText(emoji, emojiPoint.x, emojiPoint.y, emojiPaint)
        canvas.drawText(text, textPoint.x, textPoint.y, textPaint)
        canvas.restoreToCount(canvasCount)
    }

}

