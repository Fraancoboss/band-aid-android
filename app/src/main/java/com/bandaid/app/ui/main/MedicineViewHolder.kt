package com.bandaid.app.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.bandaid.app.R
import com.bandaid.app.databinding.ItemMedicineBinding
import com.bandaid.app.domain.model.Medicine

class MedicineViewHolder(
    private val binding: ItemMedicineBinding
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
    }
}
