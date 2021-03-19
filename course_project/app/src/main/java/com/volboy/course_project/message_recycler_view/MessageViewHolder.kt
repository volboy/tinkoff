package com.volboy.course_project.message_recycler_view

import android.app.ActionBar
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginRight
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx

class TextUi(
    val title: String,
    val message: String,
    override val viewType: Int = R.layout.in_message_item,
    override val uid: String = ""
) : ViewTyped

class MessageViewHolder(view: View, click: (View) -> Boolean) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)
    private val flexBoxLayout: FlexBoxLayout = view.findViewById(R.id.flex_box_layout)
    private val context=flexBoxLayout.context
    private var emojiView=EmojiView(flexBoxLayout.context)
    private var layoutParams=ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    init {
        view.setOnLongClickListener(click)
        layoutParams.rightMargin=context.dpToPx(10F)
        layoutParams.bottomMargin=context.dpToPx(7F)
        emojiView.text="2"
        emojiView.emoji=String(Character.toChars(0x1F60E))
        emojiView.setBackgroundResource(R.drawable.emodji_view_state)
        emojiView.layoutParams=layoutParams
        flexBoxLayout.addView(emojiView)

    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message

    }
}