package com.volboy.courseproject.presentation.msgstream

import android.view.View
import android.widget.TextView
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.ItemTopicDividerBinding
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.MessageHolderFactory
import com.volboy.courseproject.recyclerview.ViewTyped

class TopicUi(
    val topicName: String,
    override val viewType: Int = R.layout.item_topic_divider,
    override val uid: String = "topic"
) : ViewTyped

class TopicViewHolder(view: View, private val messageInterface: MessageHolderFactory.MessageInterface) : BaseViewHolder<TopicUi>(view) {
    private val itemTopicDividerBinding = ItemTopicDividerBinding.bind(view)
    private val topic: TextView = itemTopicDividerBinding.topic

    init {
        topic.setOnClickListener { messageInterface.getClickedView("itIsTopic", topic.text.toString(), adapterPosition) }
    }

    override fun bind(item: TopicUi) {
        topic.text = item.topicName
    }
}