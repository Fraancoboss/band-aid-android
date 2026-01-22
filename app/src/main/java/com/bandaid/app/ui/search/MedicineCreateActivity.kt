/*
 * Responsibility:
 * - Provides a simple medicine creation form for local demo data.
 * - Saves to in-memory repositories via AppContainer.
 * - Does NOT validate or persist data.
 * Layer: UI (Activity).
 * Scope: demo-only for v0.1.x.
 *
 * Responsabilidad:
 * - Provee un formulario simple para crear medicamentos demo.
 * - Guarda en repositorios in-memory via AppContainer.
 * - NO valida ni persiste datos.
 * Capa: UI (Activity).
 * Alcance: demo para v0.1.x.
 */
package com.bandaid.app.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineCreateBinding
import com.bandaid.app.domain.model.Medicine
import java.util.UUID

class MedicineCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textPrefillNote.text = getString(R.string.create_medicine_prefill_note)
        binding.inputName.setText(intent.getStringExtra(EXTRA_NAME).orEmpty())
        binding.inputDosage.setText(intent.getStringExtra(EXTRA_DOSAGE).orEmpty())
        binding.inputInstructions.setText(intent.getStringExtra(EXTRA_INSTRUCTIONS).orEmpty())
        binding.inputDuration.setText(intent.getStringExtra(EXTRA_DURATION).orEmpty())

        binding.buttonSave.setOnClickListener {
            saveMedicine()
        }
    }

    private fun saveMedicine() {
        val medicine = Medicine(
            id = UUID.randomUUID().toString(),
            name = binding.inputName.text.toString(),
            dosage = binding.inputDosage.text.toString(),
            instructions = binding.inputInstructions.text.toString(),
            isActive = true
        )

        val container = (application as BandAidApplication).appContainer
        container.medicineRepository.upsert(medicine)
        // WHY THIS DECISION:
        // Duration is stored as UI metadata only; it does not affect domain logic.
        //
        // POR QUE ESTA DECISION:
        // La duracion se guarda como metadato UI; no afecta logica de dominio.
        container.medicineMetaStore.setDuration(
            medicine.id,
            binding.inputDuration.text.toString()
        )
        finish()
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DOSAGE = "extra_dosage"
        const val EXTRA_INSTRUCTIONS = "extra_instructions"
        const val EXTRA_DURATION = "extra_duration"
    }
}
