package com.bandaid.app.ui.medicine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineDetailBinding

class MedicineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val medicineId = intent.getStringExtra(EXTRA_MEDICINE_ID)
        if (medicineId.isNullOrBlank()) {
            showNotFound()
            return
        }

        val repository = (application as BandAidApplication)
            .appContainer
            .medicineRepository
        val medicine = repository.getById(medicineId)

        if (medicine == null) {
            showNotFound()
            return
        }

        binding.textName.text = medicine.name
        binding.textDosage.text = getString(
            R.string.medicine_dosage_format,
            medicine.dosage
        )
        binding.textInstructions.text = getString(
            R.string.medicine_instructions_format,
            medicine.instructions
        )
        val statusText = if (medicine.isActive) {
            getString(R.string.medicine_status_active)
        } else {
            getString(R.string.medicine_status_inactive)
        }
        binding.textStatus.text = getString(
            R.string.medicine_status_format,
            statusText
        )
    }

    private fun showNotFound() {
        binding.textName.text = getString(R.string.medicine_not_found)
        binding.textDosage.text = ""
        binding.textInstructions.text = ""
        binding.textStatus.text = ""
    }

    companion object {
        const val EXTRA_MEDICINE_ID = "medicine_id"
    }
}
