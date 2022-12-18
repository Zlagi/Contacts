package com.streamwide.haythemmejerbi.presentation.search.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streamwide.haythemmejerbi.data.model.Contact
import com.streamwide.haythemmejerbi.databinding.ListItemContactBinding

class ContactsAdapter : ListAdapter<Contact, ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ContactViewHolder {
        return ContactViewHolder(
            ListItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(/* position = */ position)
        holder.bind(contact = contact)
    }
}

class ContactViewHolder(
    private val binding: ListItemContactBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact) {
        binding.apply {
            nameTextView.text = contact.name
            numbersTextView.text = contact.numbers.joinToString(", ")
        }
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}
