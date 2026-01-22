package com.bandaid.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bandaid.app.databinding.ItemMedicineBinding
import com.bandaid.app.domain.model.Medicine

class MedicineListAdapter(
    private val onItemClick: (Medicine) -> Unit
) : RecyclerView.Adapter<MedicineViewHolder>() {
    private val items = mutableListOf<Medicine>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicineBinding.inflate(inflater, parent, false)
        return MedicineViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(medicines: List<Medicine>) {
        items.clear()
        items.addAll(medicines)
        notifyDataSetChanged()
    }
}
