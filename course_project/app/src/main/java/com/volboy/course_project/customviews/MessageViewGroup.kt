package com.volboy.course_project.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.volboy.course_project.R

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val imageAvatar: ImageView
    private val header: TextView
    private val message: TextView
    private val imageAvatarRect = Rect()
    private val headerRect = Rect()
    private val messageRect = Rect()
    private var mRoundRectPoint = RectF()
    private val mRoundRectPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.ev_unselected, null)
        style = Paint.Style.FILL_AND_STROKE
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        imageAvatar = findViewById(R.id.avatar)
        header = findViewById(R.id.header)
        message = findViewById(R.id.message)
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightResult: Int
        val widthResult: Int
        val headerHeight: Int
        val messageHeight: Int
        val imageAvatarWidth: Int
        val messageWidth: Int
        val imageAvatarLayoutParams = imageAvatar.layoutParams as MarginLayoutParams
        val headerLayoutParams = header.layoutParams as MarginLayoutParams
        val messageLayoutParams = message.layoutParams as MarginLayoutParams
        //обмеряем аватарку
        measureChildWithMargins(imageAvatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        imageAvatarWidth = imageAvatar.measuredWidth + imageAvatarLayoutParams.leftMargin + imageAvatarLayoutParams.rightMargin
        //обмеряем заголовок
        measureChildWithMargins(header, widthMeasureSpec, imageAvatarWidth, heightMeasureSpec, 0)
        headerHeight = header.measuredHeight + headerLayoutParams.topMargin + headerLayoutParams.bottomMargin
        //обмеряем сообщение
        measureChildWithMargins(message, widthMeasureSpec, imageAvatarWidth, heightMeasureSpec, headerHeight)
        messageWidth = message.measuredWidth + messageLayoutParams.leftMargin + messageLayoutParams.rightMargin
        messageHeight = message.measuredHeight + messageLayoutParams.topMargin + messageLayoutParams.bottomMargin
        //итого ширина и высота
        widthResult = imageAvatarWidth + messageWidth
        heightResult = headerHeight + messageHeight
        setMeasuredDimension(resolveSize(widthResult, widthMeasureSpec), resolveSize(heightResult, heightMeasureSpec))
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val imageAvatarLayoutParams = imageAvatar.layoutParams as MarginLayoutParams
        val headerLayoutParams = header.layoutParams as MarginLayoutParams
        val messageLayoutParams = message.layoutParams as MarginLayoutParams
        imageAvatarRect.left = imageAvatarLayoutParams.leftMargin
        imageAvatarRect.top = imageAvatarLayoutParams.topMargin
        imageAvatarRect.right = imageAvatarRect.left + imageAvatar.measuredWidth
        imageAvatarRect.bottom = imageAvatarRect.top + imageAvatar.measuredHeight
        imageAvatar.layout(imageAvatarRect)
        headerRect.left = headerLayoutParams.leftMargin + imageAvatarRect.right
        headerRect.top = headerLayoutParams.topMargin
        headerRect.right = headerRect.left + header.measuredWidth
        headerRect.bottom = headerRect.top + header.measuredHeight
        header.layout(headerRect)
        messageRect.left = headerRect.left
        messageRect.top = messageLayoutParams.topMargin + headerRect.bottom + headerLayoutParams.bottomMargin
        messageRect.right = messageRect.left + message.measuredWidth
        messageRect.bottom = messageRect.top + message.measuredHeight
        message.layout(messageRect)
        mRoundRectPoint.set(
            headerRect.left.toFloat() - context.dpToPx(RECT_MARGIN_LEFT),
            imageAvatar.top.toFloat(),
            messageRect.right.toFloat() + messageLayoutParams.rightMargin,
            messageRect.bottom.toFloat() + context.dpToPx(RECT_MARGIN_BOTTOM)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val canvasCount = canvas?.save()
        canvas?.drawRoundRect(mRoundRectPoint, context.dpToPx(RADIUS_RECT).toFloat(), context.dpToPx(RADIUS_RECT).toFloat(), mRoundRectPaint)
        if (canvasCount != null) {
            canvas.restoreToCount(canvasCount)
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    private fun View.layout(rect: Rect) {
        layout(rect.left, rect.top, rect.right, rect.bottom)
    }

    companion object {
        private const val RADIUS_RECT = 18.0F
        private const val RECT_MARGIN_LEFT = 13.0F
        private const val RECT_MARGIN_BOTTOM = 20.0F
    }
}