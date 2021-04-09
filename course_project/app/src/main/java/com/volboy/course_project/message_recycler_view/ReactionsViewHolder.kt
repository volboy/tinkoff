package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.FrameLayout
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx
import com.volboy.course_project.model.Reaction

class ReactionsUi(
    var reactions: MutableList<Reaction>,
    override val viewType: Int = R.layout.item_messages_reactions,
    override val uid: String = ""
) : ViewTyped

class ReactionsViewHolder(view: View, private val messageInterface: MessageHolderFactory.MessageInterface) : BaseViewHolder<ReactionsUi>(view) {
    private val flexBoxLayout: FlexBoxLayout = view.findViewById(R.id.flex_box_layout)

    override fun bind(item: ReactionsUi) {
        flexBoxLayout.removeAllViews()
        item.reactions.forEach { reaction ->
            addView(reaction.emojiCode, reaction.count) //не придумал, где сделать это. Знаю, что делать здесь это жесть
        }
    }

    private fun addView(emoji: String, countReactions: Int) {
        val context = flexBoxLayout.context
        val emojiView = EmojiView(context)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, context.dpToPx(30F))
        emojiView.setPadding(context.dpToPx(9F), context.dpToPx(4.8F), context.dpToPx(9F), context.dpToPx(4.8F))
        emojiView.setBackgroundResource(R.drawable.emoji_view_state)
        layoutParams.rightMargin = context.dpToPx(10F)
        layoutParams.bottomMargin = context.dpToPx(7F)
        emojiView.layoutParams = layoutParams
        emojiView.text = countReactions.toString()
        emojiView.emoji = emoji
        emojiView.setOnClickListener { messageInterface.getClickedView(emojiView, adapterPosition) }
        flexBoxLayout.addView(emojiView)
    }
}