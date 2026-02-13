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
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MedicineListAdapter
    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Snackbar.make(
                binding.root,
                R.string.snackbar_profile_saved,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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

        binding.fabAddMedicine.setOnClickListener {
            val intent = Intent(this, MedicineSearchActivity::class.java)
            startActivity(intent)
        }

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainUiState.Content -> {
                    adapter.submitList(state.medicines)
                    binding.layoutEmpty.visibility = android.view.View.GONE
                    binding.recyclerMedicines.visibility = android.view.View.VISIBLE
                }

                else -> {
                    adapter.submitList(emptyList())
                    binding.layoutEmpty.visibility = android.view.View.VISIBLE
                    binding.recyclerMedicines.visibility = android.view.View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
                true
            }

            R.id.action_settings -> {
                val intent = Intent(this, UserSetupActivity::class.java)
                intent.putExtra(UserSetupActivity.EXTRA_INITIAL_SETUP, false)
                settingsLauncher.launch(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
