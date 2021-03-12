package com.volboy.hw_2.message_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.volboy.hw_2.R
import com.volboy.hw_2.databinding.*
import com.volboy.hw_2.model.Message

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return when (messages[position].inMessage) {
                true -> TYPE_IN_MESSAGE_WITH_DATE
                false -> TYPE_OUT_MESSAGE_WITH_DATE
            }
        } else {
            var prevPosition = position - 1
            if (prevPosition < 0) prevPosition = 0
            if (messages[position].date == messages[prevPosition].date) {
                return when (messages[position].inMessage) {
                    true -> TYPE_IN_MESSAGE
                    false -> TYPE_OUT_MESSAGE
                }
            } else {
                return when (messages[position].inMessage) {
                    true -> TYPE_IN_MESSAGE_WITH_DATE
                    false -> TYPE_OUT_MESSAGE_WITH_DATE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IN_MESSAGE -> {
                val binding = InMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                InMessageItemViewHolder(binding.root)
            }
            TYPE_OUT_MESSAGE -> {
                val binding = OutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OutMessageItemViewHolder(binding.root)
            }
            TYPE_IN_MESSAGE_WITH_DATE -> {
                val binding = InMessageDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                InMessageDateItemViewHolder(binding.root)
            }
            TYPE_OUT_MESSAGE_WITH_DATE -> {
                val binding = OutMessageDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OutMessageDateItemViewHolder(binding.root)
            }
            else -> {
                val binding = OutMessageDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OutMessageDateItemViewHolder(binding.root)
            }


        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_IN_MESSAGE -> {
                (holder as InMessageItemViewHolder).bind(messages[position])
            }
            TYPE_OUT_MESSAGE -> {
                (holder as OutMessageItemViewHolder).bind(messages[position])
            }
            TYPE_IN_MESSAGE_WITH_DATE -> {
                (holder as InMessageDateItemViewHolder).bind(messages[position])
            }
            TYPE_OUT_MESSAGE_WITH_DATE -> {
                (holder as OutMessageDateItemViewHolder).bind(messages[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    companion object {
        private const val TYPE_IN_MESSAGE = 0
        private const val TYPE_OUT_MESSAGE = 1
        private const val TYPE_IN_MESSAGE_WITH_DATE = 2
        private const val TYPE_OUT_MESSAGE_WITH_DATE = 3
    }

    inner class InMessageItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtHeader: TextView = itemView.findViewById(R.id.header)
        var txtMessage: TextView = itemView.findViewById(R.id.message)

        fun bind(message: Message) {
            txtHeader.text = message.header
            txtMessage.text = message.textMessage
        }
    }

    inner class OutMessageItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtMessage: TextView = itemView.findViewById(R.id.message)

        fun bind(message: Message) {
            txtMessage.text = message.textMessage
        }
    }

    inner class InMessageDateItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtHeader: TextView = itemView.findViewById(R.id.header)
        var txtMessage: TextView = itemView.findViewById(R.id.message)
        var txtDate: TextView = itemView.findViewById(R.id.date)

        fun bind(message: Message) {
            txtHeader.text = message.header
            txtMessage.text = message.textMessage
            txtDate.text = message.date
        }
    }

    inner class OutMessageDateItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtMessage: TextView = itemView.findViewById(R.id.message)
        var txtDate: TextView = itemView.findViewById(R.id.date)

        fun bind(message: Message) {
            txtMessage.text = message.textMessage
            txtDate.text = message.date
        }
    }

}