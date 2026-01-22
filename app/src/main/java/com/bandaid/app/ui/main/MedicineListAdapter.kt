/*
 * Responsibility:
 * - RecyclerView adapter for read-only medicine list.
 * - Emits item click callbacks to the Activity.
 * - Does NOT mutate data or perform formatting beyond binding.
 * Layer: UI (adapter).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Adapter de RecyclerView para lista de medicinas en solo lectura.
 * - Emite callbacks de click hacia la Activity.
 * - NO modifica datos ni hace formato mas alla del binding.
 * Capa: UI (adapter).
 * Alcance: estable para v0.1.
 */
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
