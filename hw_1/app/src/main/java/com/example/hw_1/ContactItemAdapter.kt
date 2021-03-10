package com.example.hw_1


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.hw_1.databinding.ContactItemBinding

class ContactItemAdapter(private val contacts: List<ContactsData>) : Adapter<ContactItemAdapter.ContactItemViewHolder>() {
    private lateinit var binding: ContactItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemView = binding.root
        return ContactItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        holder.txtViewName.text = contacts[position].name
        holder.txtViewNumber.text = contacts[position].number
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    inner class ContactItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var txtViewName: TextView = binding.txtViewName
        var txtViewNumber: TextView = binding.txtViewNumber

        fun bind(item: ContactsData) {
            txtViewName.text = item.name
            txtViewNumber.text = item.number
        }
    }
}

