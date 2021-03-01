package com.example.hw_1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtViewName: TextView = itemView.findViewById(R.id.txtViewName)
    var txtViewNumber: TextView = itemView.findViewById(R.id.txtViewNumber)

    fun bind(item: ContactsData) {
        txtViewName.text = item.name
        txtViewNumber.text = item.number

    }

}