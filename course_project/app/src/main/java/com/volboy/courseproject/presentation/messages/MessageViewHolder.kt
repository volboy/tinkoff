package com.volboy.courseproject.presentation.messages

import android.text.Spanned
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.volboy.courseproject.R
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.MessageHolderFactory
import com.volboy.courseproject.recyclerview.ViewTyped

class TextUi(
    val title: String,
    val message: Spanned?,
    val imageURL: String,
    override val viewType: Int = R.layout.item_in_message,
    override val uid: String = ""
) : ViewTyped

class MessageViewHolder(val view: View, private val messageInterface: MessageHolderFactory.MessageInterface) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)
    private val image: ShapeableImageView = view.findViewById(R.id.avatar)

    init {
        view.setOnLongClickListener {
            messageInterface.getLongClickedView(adapterPosition)
        }
    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message
        Glide.with(image.context)
            .load(item.imageURL)
            .fitCenter()
            .error(R.drawable.ic_profile)
            .into(image)
    }
}

