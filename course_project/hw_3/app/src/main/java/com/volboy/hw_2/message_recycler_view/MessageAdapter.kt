package com.volboy.hw_2.message_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.volboy.hw_2.R
import com.volboy.hw_2.databinding.ItemMessageBinding

class MessageAdapter(private val messages: List<String>) : RecyclerView.Adapter<MessageAdapter.MessageItemViewHolder>() {
    private lateinit var binding: ItemMessageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemView = binding.root
        return MessageItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        holder.txtMessage.text = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtMessage: TextView = itemView.findViewById(R.id.message)

        fun bind(item: String) {
            txtMessage.text = item
        }
    }
}