package com.volboy.course_project.message_recycler_view

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.model.Reaction

class ReactionsUi(
    var reactions: MutableList<Reaction>,
    override val viewType: Int = R.layout.item_messages_reactions,
    override val uid: String = ""
) : ViewTyped

class ReactionsViewHolder(view: View, private val messageInterface: MessageHolderFactory.MessageInterface) : BaseViewHolder<ReactionsUi>(view) {
    private val flexBoxLayout: FlexBoxLayout = view.findViewById(R.id.flex_box_layout)
    private val context = flexBoxLayout.context

    override fun bind(item: ReactionsUi) {
        flexBoxLayout.removeAllViews()
        item.reactions.forEach { reaction ->
            addView(reaction.emojiCode, reaction.users.size, false) //не придумал, где сделать это. Знаю, что делать здесь это жесть
        }
        if (item.reactions.size != 0) {
            addView(context.getString(R.string.last_emoji_str), 0, true)
        }
    }

    private fun addView(emoji: String, countReactions: Int, isLastView: Boolean) {
        val emojiView = EmojiView(context)
        emojiView.text = countReactions.toString()
        emojiView.emoji = emoji
        emojiView.setOnClickListener { messageInterface.getClickedView(emojiView, adapterPosition) }
        if (isLastView) {
            flexBoxLayout.addLastView(emojiView)
        } else {
            flexBoxLayout.addView(emojiView)
        }
    }
}
