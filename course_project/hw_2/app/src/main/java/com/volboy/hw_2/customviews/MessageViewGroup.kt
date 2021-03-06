package com.volboy.hw_2.customviews

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.volboy.hw_2.R
import java.nio.file.FileVisitOption

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val imageAvatar:ImageView
    private val header:TextView
    private  val message:TextView
    private val flexBoxLayout: LinearLayout

    private val imageAvatarRect=Rect()
    private val headerRect= Rect()
    private  val messageRect=Rect()
    private val flexBoxLayoutRect=Rect()



    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        imageAvatar=findViewById(R.id.avatar)
        header=findViewById(R.id.header_title)
        message=findViewById(R.id.message)
        flexBoxLayout=findViewById(R.id.flex_box_layout)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height=0
        var width=0
        var imageAvatarHeight=0
        var headerHeight=0
        var messageHeight=0
        var flexBoxLayoutHeight=0
        var imageAvatarWidth=0
        var headerWidth=0
        var messageWidth=0
        var flexBoxLayoutWidth=0
        val imageAvatarLayoutParams=imageAvatar.layoutParams as MarginLayoutParams
        val headerLayoutParams=header.layoutParams as MarginLayoutParams
        val messageLayoutParams=message.layoutParams as MarginLayoutParams
        val flexBoxLayoutParams=flexBoxLayout.layoutParams as MarginLayoutParams

        measureChildWithMargins(imageAvatar, widthMeasureSpec,0,heightMeasureSpec,0)
        imageAvatarWidth=imageAvatar.measuredWidth + imageAvatarLayoutParams.leftMargin + imageAvatarLayoutParams.rightMargin
        imageAvatarHeight=imageAvatar.measuredHeight + imageAvatarLayoutParams.topMargin + imageAvatarLayoutParams.bottomMargin

        measureChildWithMargins(header, widthMeasureSpec,imageAvatarWidth,heightMeasureSpec,0)
        headerWidth=header.measuredWidth + headerLayoutParams.leftMargin + headerLayoutParams.rightMargin
        headerHeight=header.measuredHeight + headerLayoutParams.topMargin + headerLayoutParams.bottomMargin

        measureChildWithMargins(message, widthMeasureSpec,imageAvatarWidth,heightMeasureSpec,headerHeight)
        messageWidth=message.measuredWidth + messageLayoutParams.leftMargin + messageLayoutParams.rightMargin
        messageHeight=message.measuredHeight + messageLayoutParams.topMargin + messageLayoutParams.bottomMargin

        measureChildWithMargins(flexBoxLayout, widthMeasureSpec,imageAvatarWidth,heightMeasureSpec,headerHeight+messageHeight)
        flexBoxLayoutWidth=flexBoxLayout.measuredWidth + flexBoxLayoutParams.leftMargin + flexBoxLayoutParams.rightMargin
        flexBoxLayoutHeight=flexBoxLayout.measuredHeight + flexBoxLayoutParams.topMargin + flexBoxLayoutParams.bottomMargin

        width=imageAvatarWidth+messageWidth
        height=headerHeight+messageHeight+flexBoxLayoutHeight
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec))








    }
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val imageAvatarLayoutParams=imageAvatar.layoutParams as MarginLayoutParams
        val headerLayoutParams=header.layoutParams as MarginLayoutParams
        val messageLayoutParams=message.layoutParams as MarginLayoutParams
        val flexBoxLayoutParams=flexBoxLayout.layoutParams as MarginLayoutParams

        imageAvatarRect.left=imageAvatarLayoutParams.leftMargin
        imageAvatarRect.top=imageAvatarLayoutParams.topMargin
        imageAvatarRect.right=imageAvatarRect.left + imageAvatar.measuredWidth
        imageAvatarRect.bottom= imageAvatarRect.top + imageAvatar.measuredHeight
        imageAvatar.layout(imageAvatarRect)

        headerRect.left=headerLayoutParams.leftMargin + imageAvatarRect.right + imageAvatarLayoutParams.rightMargin
        headerRect.top=headerLayoutParams.topMargin
        headerRect.right=headerRect.left + header.measuredWidth
        headerRect.bottom=headerRect.top + header.measuredHeight
        header.layout(headerRect)

        messageRect.left=headerRect.left
        messageRect.top=messageLayoutParams.topMargin + headerRect.bottom + headerLayoutParams.bottomMargin
        messageRect.right= messageRect.left + message.measuredWidth
        messageRect.bottom=messageRect.top + message.measuredHeight
        message.layout(messageRect)

        flexBoxLayoutRect.left=headerRect.left
        flexBoxLayoutRect.top=flexBoxLayoutParams.topMargin + messageRect.bottom + messageLayoutParams.bottomMargin
        flexBoxLayoutRect.right=flexBoxLayoutRect.left + flexBoxLayout.measuredWidth
        flexBoxLayoutRect.bottom=flexBoxLayoutRect.top + flexBoxLayout.measuredHeight
        flexBoxLayout.layout(flexBoxLayoutRect)


    }

    override fun generateDefaultLayoutParams(): LayoutParams=MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams=MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams=MarginLayoutParams(p)

    private fun View.layout(rect:Rect){
        layout(rect.left, rect.top, rect.right, rect.bottom)
    }

}