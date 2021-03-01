package com.example.hw_1


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class ContactItemAdapter(private val contacts: List<ContactsData>) :
    Adapter<ContactItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val itemView =
            LayoutInflater.from(parent?.context).inflate(R.layout.contact_item, parent, false)
        return ContactItemViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        holder.txtViewName?.text = contacts[position].name
        holder.txtViewNumber?.text = contacts[position].number
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}