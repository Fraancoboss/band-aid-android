package com.bandaid.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bandaid.app.databinding.ItemMedicineBinding
import com.bandaid.app.domain.model.Medicine

class MedicineListAdapter(
    private val onItemClick: (Medicine) -> Unit
) : ListAdapter<Medicine, MedicineViewHolder>(MedicineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicineBinding.inflate(inflater, parent, false)
        return MedicineViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class MedicineDiffCallback : DiffUtil.ItemCallback<Medicine>() {
    override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
        oldItem == newItem
}
