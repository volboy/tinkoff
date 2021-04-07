package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R

class TextUi(
    val title: String,
    val message: String,
    override val viewType: Int = R.layout.item_in_message,
    override val uid: String = ""
) : ViewTyped

class MessageViewHolder(val view: View, private val messageInterface: MessageHolderFactory.MessageInterface) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)

    init {
        view.setOnLongClickListener {
            messageInterface.getLongClickedView(adapterPosition)
        }
    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message
    }
}

