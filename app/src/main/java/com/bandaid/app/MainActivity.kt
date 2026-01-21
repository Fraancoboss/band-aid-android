package com.bandaid.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.databinding.ActivityMainBinding
import com.bandaid.app.ui.main.MainUiState
import com.bandaid.app.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    state.medicineCount
                )
            }
            binding.textStatus.text = text
        }
    }
}
