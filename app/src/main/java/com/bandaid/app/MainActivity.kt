/*
 * Responsibility:
 * - UI entry point showing app status and list of active medicines.
 * - Redirects to user setup when no profile exists in memory.
 * - Delegates data access to ViewModel and in-memory repositories via AppContainer.
 * - Does NOT implement persistence, background work, or domain validation.
 * Layer: UI (Activity).
 * Scope: stable for v0.1.x.
 *
 * Responsabilidad:
 * - Punto de entrada de UI que muestra estado y lista de medicinas activas.
 * - Redirige a setup de usuario cuando no hay perfil en memoria.
 * - Delega acceso a datos al ViewModel y repositorios in-memory via AppContainer.
 * - NO implementa persistencia, trabajo en segundo plano ni validacion de dominio.
 * Capa: UI (Activity).
 * Alcance: estable para v0.1.x.
 */
package com.bandaid.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bandaid.app.databinding.ActivityMainBinding
import com.bandaid.app.ui.calendar.CalendarActivity
import com.bandaid.app.ui.medicine.MedicineDetailActivity
import com.bandaid.app.ui.main.MainUiState
import com.bandaid.app.ui.main.MainViewModel
import com.bandaid.app.ui.main.MedicineListAdapter
import com.bandaid.app.ui.search.MedicineSearchActivity
import com.bandaid.app.ui.user.UserSetupActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MedicineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val container = (application as BandAidApplication).appContainer
        val profile = container.userProfileStore.getProfile()
        if (profile == null) {
            // WHY THIS DECISION:
            // User setup is mandatory when no profile exists in memory.
            //
            // POR QUE ESTA DECISION:
            // El setup es obligatorio cuando no existe perfil en memoria.
            val intent = Intent(this, UserSetupActivity::class.java)
            intent.putExtra(UserSetupActivity.EXTRA_INITIAL_SETUP, true)
            startActivity(intent)
            finish()
            return
        }

        // WHY THIS DECISION:
        // RecyclerView is used only for the medicines list; other screens are small lists.
        //
        // POR QUE ESTA DECISION:
        // RecyclerView se usa solo para la lista de medicinas; otras pantallas son listas pequenas.
        adapter = MedicineListAdapter { medicine ->
            val intent = Intent(this, MedicineDetailActivity::class.java)
            intent.putExtra(MedicineDetailActivity.EXTRA_MEDICINE_ID, medicine.id)
            startActivity(intent)
        }
        binding.recyclerMedicines.layoutManager = LinearLayoutManager(this)
        binding.recyclerMedicines.adapter = adapter

        // WHY THIS DECISION:
        // Repositories are resolved from AppContainer to avoid instantiation in Activities.
        //
        // POR QUE ESTA DECISION:
        // Los repositorios se obtienen del AppContainer para evitar instancias en Activities.
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

        binding.buttonOpenCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSearchMedicine.setOnClickListener {
            val intent = Intent(this, MedicineSearchActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, UserSetupActivity::class.java)
            intent.putExtra(UserSetupActivity.EXTRA_INITIAL_SETUP, false)
            startActivity(intent)
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
