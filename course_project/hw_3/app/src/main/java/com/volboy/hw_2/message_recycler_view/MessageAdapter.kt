package com.volboy.hw_2.message_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.volboy.hw_2.R
import com.volboy.hw_2.databinding.DateDividerItemBinding
import com.volboy.hw_2.databinding.InMessageItemBinding
import com.volboy.hw_2.databinding.OutMessageItemBinding
import com.volboy.hw_2.model.Messege

class MessageAdapter(private val messages: List<Messege>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        var nextPosition=position+1
        if (nextPosition==messages.size) nextPosition=messages.size-1
        if (messages[position].date!=messages[nextPosition].date)
            return TYPE_DIVIDER
        return when (messages[position].outMessage) {
            true -> TYPE_IN_MESSAGE
            false -> TYPE_OUT_MESSAGE
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
            TYPE_DIVIDER -> {
                val binding = DateDividerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DividerItemViewHolder(binding.root)
            }
            else -> {
                val binding = DateDividerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DividerItemViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_IN_MESSAGE -> {
                (holder as InMessageItemViewHolder).txtHeader.text = messages[position].header
                holder.txtMessage.text = messages[position].textMessage
            }
            TYPE_OUT_MESSAGE -> {
                (holder as OutMessageItemViewHolder).txtMessage.text = messages[position].textMessage
            }
            TYPE_DIVIDER -> {
                (holder as DividerItemViewHolder).txtDate.text = messages[position].date
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    companion object {
        private const val TYPE_IN_MESSAGE = 0
        private const val TYPE_OUT_MESSAGE = 1
        private const val TYPE_DIVIDER = 2
    }

    inner class InMessageItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtHeader: TextView = itemView.findViewById(R.id.header)
        var txtMessage: TextView = itemView.findViewById(R.id.message)
    }

    inner class OutMessageItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtMessage: TextView = itemView.findViewById(R.id.message)
    }

    inner class DividerItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtDate: TextView = itemView.findViewById(R.id.date)
    }
}