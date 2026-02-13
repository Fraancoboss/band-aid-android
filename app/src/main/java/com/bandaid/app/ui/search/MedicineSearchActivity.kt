/*
 * Responsibility:
 * - Provides a local stub search for demo medicines.
 * - Filters a hardcoded list and routes to the create form.
 * - Does NOT call external services or persist data.
 * Layer: UI (Activity).
 * Scope: demo-only for v0.1.x.
 *
 * Responsabilidad:
 * - Provee una busqueda local stub de medicinas demo.
 * - Filtra una lista hardcoded y navega al formulario de creacion.
 * - NO usa servicios externos ni persistencia.
 * Capa: UI (Activity).
 * Alcance: demo para v0.1.x.
 */
package com.bandaid.app.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineSearchBinding
import com.bandaid.app.databinding.ItemMedicineSuggestionBinding
import com.google.android.material.snackbar.Snackbar

class MedicineSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineSearchBinding
    private val createLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val messageRes = result.data?.getIntExtra(
                MedicineCreateActivity.EXTRA_RESULT_MESSAGE_RES,
                0
            ) ?: 0
            if (messageRes != 0) {
                Snackbar.make(binding.root, messageRes, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // WHY THIS DECISION:
    // This is a local stub list; external databases (AEMPS/CIMA) are out of v0.1.x.
    //
    // POR QUE ESTA DECISION:
    // Esta lista es un stub local; BD externas (AEMPS/CIMA) quedan fuera de v0.1.x.
    private val suggestions = listOf(
        MedicineSuggestion("Paracetamol", "500 mg", "Cada 8 horas"),
        MedicineSuggestion("Ibuprofeno", "400 mg", "Con comida"),
        MedicineSuggestion("Amoxicilina", "500 mg", "Cada 12 horas"),
        MedicineSuggestion("Loratadina", "10 mg", "Una vez al dia"),
        MedicineSuggestion("Omeprazol", "20 mg", "Antes del desayuno"),
        MedicineSuggestion("Metformina", "850 mg", "Con comida"),
        MedicineSuggestion("Atorvastatina", "20 mg", "Por la noche")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.textStubNote.text = getString(R.string.search_stub_note)

        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                renderResults(s?.toString().orEmpty())
            }
        })

        renderResults("")
    }

    private fun renderResults(query: String) {
        val normalized = query.trim().lowercase()
        val results = if (normalized.isEmpty()) {
            suggestions
        } else {
            // WHY THIS DECISION:
            // Case-insensitive contains matching keeps the stub simple and predictable.
            //
            // POR QUE ESTA DECISION:
            // La busqueda contiene (sin mayusculas) mantiene el stub simple y predecible.
            suggestions.filter { it.name.lowercase().contains(normalized) }
        }

        binding.layoutResults.removeAllViews()
        if (results.isEmpty()) {
            binding.textEmpty.visibility = View.VISIBLE
            return
        }

        binding.textEmpty.visibility = View.GONE
        results.forEach { suggestion ->
            val itemBinding = ItemMedicineSuggestionBinding.inflate(
                layoutInflater,
                binding.layoutResults,
                false
            )
            itemBinding.textName.text = suggestion.name
            itemBinding.textDosage.text = getString(
                R.string.medicine_dosage_format,
                suggestion.dosage
            )
            itemBinding.textInstructions.text = getString(
                R.string.medicine_instructions_format,
                suggestion.instructions
            )
            itemBinding.root.setOnClickListener {
                openCreateForm(suggestion)
            }
            binding.layoutResults.addView(itemBinding.root)
        }
    }

    private fun openCreateForm(suggestion: MedicineSuggestion) {
        // WHY THIS DECISION:
        // This is a local stub; no external database is consulted in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // Esto es un stub local; no se consulta ninguna BD externa en v0.1.x.
        val intent = Intent(this, MedicineCreateActivity::class.java)
        intent.putExtra(MedicineCreateActivity.EXTRA_NAME, suggestion.name)
        intent.putExtra(MedicineCreateActivity.EXTRA_DOSAGE, suggestion.dosage)
        intent.putExtra(MedicineCreateActivity.EXTRA_INSTRUCTIONS, suggestion.instructions)
        createLauncher.launch(intent)
    }

    private data class MedicineSuggestion(
        val name: String,
        val dosage: String,
        val instructions: String
    )
}
