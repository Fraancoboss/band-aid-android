package com.bandaid.app

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bandaid.app.databinding.ActivityMainBinding
import com.bandaid.app.ui.medicine.MedicineDetailActivity
import com.bandaid.app.ui.main.MainUiState
import com.bandaid.app.ui.main.MainViewModel
import com.bandaid.app.ui.main.MedicineListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MedicineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MedicineListAdapter { medicine ->
            val intent = Intent(this, MedicineDetailActivity::class.java)
            intent.putExtra(MedicineDetailActivity.EXTRA_MEDICINE_ID, medicine.id)
            startActivity(intent)
        }
        binding.recyclerMedicines.layoutManager = LinearLayoutManager(this)
        binding.recyclerMedicines.adapter = adapter

        val container = (application as BandAidApplication).appContainer
        viewModel = ViewModelProvider(
            this,
            container.mainViewModelFactory
        )[MainViewModel::class.java]

        binding.buttonAddDemo.setOnClickListener {
            viewModel.addDemoMedicine(
                name = getString(R.string.demo_medicine_name),
                dosage = getString(R.string.demo_medicine_dosage),
                instructions = getString(R.string.demo_medicine_instructions)
            )
        }

        viewModel.uiState.observe(this) { state ->
            val text = when (state) {
                is MainUiState.Loading -> getString(R.string.status_loading)
                is MainUiState.Empty -> getString(R.string.status_empty)
                is MainUiState.Content -> getString(
                    R.string.status_content,
                    state.medicines.size
                )
            }
            binding.textStatus.text = text
            when (state) {
                is MainUiState.Content -> adapter.submitList(state.medicines)
                else -> adapter.submitList(emptyList())
            }
        }
    }
}
