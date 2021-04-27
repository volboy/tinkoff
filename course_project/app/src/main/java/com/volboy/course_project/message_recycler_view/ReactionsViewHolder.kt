package com.volboy.course_project.message_recycler_view

import android.view.View
import com.volboy.course_project.MainActivity.Companion.ownId
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
            if (reaction.users.contains(ownId)) {
                addView(reaction.emojiCode, reaction.emojiName, reaction.users.size, isLastView = false, ownEmoji = true)
            } else {
                addView(reaction.emojiCode, reaction.emojiName, reaction.users.size, isLastView = false, ownEmoji = false)
            }
        }
        if (item.reactions.size != 0) {
            addView(context.getString(R.string.last_emoji_str), "", 0, true, ownEmoji = false)
        }
    }

    private fun addView(emojiCode: String, emojiName: String, countReactions: Int, isLastView: Boolean, ownEmoji: Boolean) {
        val emojiView = EmojiView(context)
        emojiView.text = countReactions.toString()
        emojiView.emoji = emojiCode
        emojiView.setOnClickListener { messageInterface.getClickedEmoji(emojiCode, emojiName, adapterPosition) }
        emojiView.isSelected = ownEmoji
        if (isLastView) {
            flexBoxLayout.addLastView(emojiView)
        } else {
            flexBoxLayout.addView(emojiView)
        }
    }
}
