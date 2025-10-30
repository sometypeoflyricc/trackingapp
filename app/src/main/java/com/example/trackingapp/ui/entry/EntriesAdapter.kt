package com.example.trackingapp.ui.entry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.data.Entry
import com.example.trackingapp.databinding.ItemEntryBinding

/**
 * Bindable list of [Entry] items with callbacks for deletion and editing.
 * Uses [DiffUtil] for efficient updates.
 */
class EntriesAdapter(
    private val onDelete: (Entry) -> Unit,
    private val onEdit: (Entry) -> Unit
) : ListAdapter<Entry, EntriesAdapter.VH>(Diff) {

    /** Identity by id, structural equality for contents. */
    object Diff : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(oldItem: Entry, newItem: Entry) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Entry, newItem: Entry) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding, onDelete, onEdit)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    /** Simple ViewHolder that binds amount, note and date. Row tap edits, button deletes. */
    class VH(
        private val b: ItemEntryBinding,
        private val onDelete: (Entry) -> Unit,
        private val onEdit: (Entry) -> Unit
    ) : RecyclerView.ViewHolder(b.root) {

        /** Fills the user interface from [e] and connects click handlers. */
        fun bind(e: Entry) {
            b.tvAmount.text = "${e.amount} min"
            b.tvNote.text = e.note ?: ""
            b.tvDate.text = e.date.toString()

            b.btnDelete.setOnClickListener { onDelete(e) }
            b.root.setOnClickListener { onEdit(e) }   // tap the row to edit
        }
    }
}

