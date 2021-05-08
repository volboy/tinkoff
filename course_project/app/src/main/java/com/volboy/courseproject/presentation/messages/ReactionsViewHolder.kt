package com.volboy.courseproject.presentation.messages

import android.view.View
import com.volboy.courseproject.MainPresenter.Companion.ownId
import com.volboy.courseproject.R
import com.volboy.courseproject.customviews.EmojiView
import com.volboy.courseproject.customviews.FlexBoxLayout
import com.volboy.courseproject.model.Reaction
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.MessageHolderFactory
import com.volboy.courseproject.recyclerview.ViewTyped

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
        emojiView.setOnClickListener { messageInterface.getClickedView(emojiCode, emojiName, adapterPosition) }
        emojiView.isSelected = ownEmoji
        if (isLastView) {
            flexBoxLayout.addLastView(emojiView)
        } else {
            flexBoxLayout.addView(emojiView)
        }
    }
}
