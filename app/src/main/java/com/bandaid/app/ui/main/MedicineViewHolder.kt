/*
 * Responsibility:
 * - Binds a Medicine to the list item view.
 * - Formats strings using resources only.
 * - Does NOT handle navigation or state changes.
 * Layer: UI (view holder).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Vincula un Medicine al item de la lista.
 * - Formatea textos usando solo recursos.
 * - NO maneja navegacion ni cambios de estado.
 * Capa: UI (view holder).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.bandaid.app.R
import com.bandaid.app.databinding.ItemMedicineBinding
import com.bandaid.app.domain.model.Medicine

class MedicineViewHolder(
    private val binding: ItemMedicineBinding,
    private val onItemClick: (Medicine) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Medicine) {
        val context = binding.root.context
        binding.textName.text = item.name
        binding.textDosage.text = context.getString(
            R.string.medicine_dosage_format,
            item.dosage
        )
        binding.textInstructions.text = context.getString(
            R.string.medicine_instructions_format,
            item.instructions
        )
        binding.root.setOnClickListener { onItemClick(item) }
    }
}
