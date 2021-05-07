package com.volboy.courseproject.recyclerview

import android.view.View
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.messages.DataViewHolder
import com.volboy.courseproject.presentation.messages.MessageViewHolder
import com.volboy.courseproject.presentation.messages.ReactionsViewHolder

class MessageHolderFactory(private val messageInterface: MessageInterface) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_in_message -> MessageViewHolder(view, messageInterface)
            R.layout.item_out_message -> MessageViewHolder(view, messageInterface)
            R.layout.item_date_divider -> DataViewHolder(view)
            R.layout.item_messages_reactions -> ReactionsViewHolder(view, messageInterface)
            R.layout.item_messages_reactions_out -> ReactionsViewHolder(view, messageInterface)
            else -> null
        }
    }

    interface MessageInterface {
        fun getLongClickedView(position: Int): Boolean
        fun getClickedEmoji(emojiCode: String, emojiName: String, position: Int)
    }
}