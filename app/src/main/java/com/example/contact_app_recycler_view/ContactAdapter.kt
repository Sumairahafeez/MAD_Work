package com.example.contact_app_recycler_view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val listener: OnContactActionListener
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var filteredList: MutableList<Contact> = contactList.toMutableList()
    var isGridView: Boolean = true

    interface OnContactActionListener {
        fun onItemClick(position: Int)
        fun onEditClick(position: Int, contact: Contact)
        fun onDeleteClick(position: Int, contact: Contact)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivContactImage: ImageView = itemView.findViewById(R.id.ivContactImage)
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tvContactPhone)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutId = if (isGridView) R.layout.activity_item_contact else R.layout.activity_item_contact_list
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = filteredList[position]

        holder.tvContactName.text = currentContact.name
        holder.tvContactPhone.text = currentContact.phone

        if (currentContact.imageUri != null) {
            holder.ivContactImage.setImageURI(Uri.parse(currentContact.imageUri))
        } else {
            holder.ivContactImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(position, currentContact)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(position, currentContact)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateList(newList: List<Contact>) {
        contactList = newList.toMutableList()
        filter("")
    }

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredList = if (lowerCaseQuery.isEmpty()) {
            contactList.toMutableList()
        } else {
            contactList.filter {
                it.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                it.phone.contains(lowerCaseQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
