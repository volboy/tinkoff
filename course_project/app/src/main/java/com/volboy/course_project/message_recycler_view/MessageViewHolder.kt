package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx
import com.volboy.course_project.model.Reaction

class TextUi(
    val title: String,
    val message: String,
    val reactions: List<Reaction>?,
    override val viewType: Int = R.layout.in_message_item,
    override val uid: String = ""
) : ViewTyped

class MessageViewHolder(view: View, click: (View) -> Boolean) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)
    private var reactions= mutableListOf<Reaction>()
    private val flexBoxLayout: FlexBoxLayout = view.findViewById(R.id.flex_box_layout)
    private val emojiView = EmojiView(flexBoxLayout.context)
    private val context = flexBoxLayout.context
    private val clickEmoji: (View) -> Unit = { view ->
        view.isSelected = !view.isSelected
    }

    init {
        view.setOnLongClickListener(click)
        val layoutParams = FrameLayout.LayoutParams(context.dpToPx(45F), context.dpToPx(30F))
        layoutParams.rightMargin = context.dpToPx(10F)
        layoutParams.bottomMargin = context.dpToPx(7F)
        emojiView.setPadding(context.dpToPx(9F), context.dpToPx(4.8F), context.dpToPx(9F), context.dpToPx(4.8F))
        emojiView.setBackgroundResource(R.drawable.emodji_view_state)
        emojiView.layoutParams = layoutParams
    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message
        if (item.reactions != null) {
            item.reactions.forEach { reaction ->
                emojiView.text = reaction.count.toString()
                emojiView.emoji = reaction.emoji
                emojiView.setOnClickListener(clickEmoji)
                flexBoxLayout.addView(emojiView)
            }
        }
    }
}

