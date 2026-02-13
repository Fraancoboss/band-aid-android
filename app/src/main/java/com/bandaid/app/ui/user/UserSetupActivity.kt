/*
 * Responsibility:
 * - Collects a local-only user profile during initial setup or settings access.
 * - Stores profile in memory via AppContainer.
 * - Does NOT perform medical validation or persistence.
 * Layer: UI (Activity).
 * Scope: demo-only for v0.1.x.
 *
 * Responsabilidad:
 * - Recoge un perfil de usuario local en setup inicial o settings.
 * - Guarda el perfil en memoria via AppContainer.
 * - NO realiza validacion medica ni persistencia.
 * Capa: UI (Activity).
 * Alcance: demo para v0.1.x.
 */
package com.bandaid.app.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.MainActivity
import com.bandaid.app.R
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.bandaid.app.data.local.UserProfile
import com.bandaid.app.databinding.ActivityUserSetupBinding
import com.bandaid.app.domain.model.User
import java.util.UUID
import java.util.TimeZone

class UserSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSetupBinding
    private lateinit var sexOptions: Array<String>
    private var isInitialSetup: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        isInitialSetup = intent.getBooleanExtra(EXTRA_INITIAL_SETUP, false)
        if (!isInitialSetup) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            binding.toolbar.setNavigationOnClickListener { finish() }
        }

        // WHY THIS DECISION:
        // A fixed spinner keeps the field descriptive and avoids free-text ambiguity.
        //
        // POR QUE ESTA DECISION:
        // Un spinner fijo mantiene el campo descriptivo y evita texto libre ambiguo.
        sexOptions = resources.getStringArray(R.array.user_sex_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sexOptions)
        binding.dropdownSex.setAdapter(adapter)

        val container = (application as BandAidApplication).appContainer
        val existingProfile = container.userProfileStore.getProfile()
        if (existingProfile != null) {
            binding.inputAge.setText(existingProfile.age.toString())
            binding.inputWeight.setText(existingProfile.weight.toString())
            binding.inputHeight.setText(existingProfile.height.toString())
            binding.dropdownSex.setText(existingProfile.sex, false)
            binding.inputDiseases.setText(existingProfile.diseases.orEmpty())
        } else if (sexOptions.isNotEmpty()) {
            binding.dropdownSex.setText(sexOptions.first(), false)
        }

        binding.buttonSaveContinue.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        binding.layoutAge.error = null
        binding.layoutWeight.error = null
        binding.layoutHeight.error = null
        binding.layoutSex.error = null

        // WHY THIS DECISION:
        // Basic parsing keeps the flow simple; no medical validation is performed in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // El parseo basico mantiene el flujo simple; no hay validacion medica en v0.1.x.
        val age = binding.inputAge.text?.toString()?.trim()?.toIntOrNull()
        val weight = binding.inputWeight.text?.toString()?.trim()?.toFloatOrNull()
        val height = binding.inputHeight.text?.toString()?.trim()?.toFloatOrNull()
        val sex = binding.dropdownSex.text?.toString()?.trim().orEmpty()

        var hasError = false
        if (age == null || age !in 1..150) {
            binding.layoutAge.error = getString(R.string.error_invalid_age)
            hasError = true
        }
        if (weight == null || weight <= 0f) {
            binding.layoutWeight.error = getString(R.string.error_invalid_weight)
            hasError = true
        }
        if (height == null || height <= 0f) {
            binding.layoutHeight.error = getString(R.string.error_invalid_height)
            hasError = true
        }
        if (sex.isBlank()) {
            binding.layoutSex.error = getString(R.string.error_field_required)
            hasError = true
        }
        if (hasError) return

        // WHY THIS DECISION:
        // The sex field is descriptive only and does not imply medical logic in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // El campo sexo es descriptivo y no implica logica medica en v0.1.x.
        val diseases = binding.inputDiseases.text.toString().ifBlank { null }

        val profile = UserProfile(
            age = age!!,
            weight = weight!!,
            height = height!!,
            sex = sex,
            diseases = diseases
        )

        val container = (application as BandAidApplication).appContainer
        container.userProfileStore.saveProfile(profile)

        // WHY THIS DECISION:
        // User is stored as an identity marker without extra profile fields in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // User se guarda como identidad sin campos extra en v0.1.x.
        val user = User(
            id = UUID.randomUUID().toString(),
            name = getString(R.string.user_default_name),
            timezone = TimeZone.getDefault().id
        )
        container.userRepository.upsert(user)

        Snackbar.make(
            binding.root,
            R.string.snackbar_profile_saved,
            Snackbar.LENGTH_SHORT
        ).show()

        if (isInitialSetup) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            setResult(RESULT_OK)
        }
        finish()
    }

    companion object {
        const val EXTRA_INITIAL_SETUP = "extra_initial_setup"
    }
}
