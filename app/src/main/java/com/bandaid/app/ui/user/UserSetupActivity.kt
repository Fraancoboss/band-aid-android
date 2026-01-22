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
import com.bandaid.app.data.local.UserProfile
import com.bandaid.app.databinding.ActivityUserSetupBinding
import com.bandaid.app.domain.model.User
import java.util.UUID
import java.util.TimeZone

class UserSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WHY THIS DECISION:
        // A fixed spinner keeps the field descriptive and avoids free-text ambiguity.
        //
        // POR QUE ESTA DECISION:
        // Un spinner fijo mantiene el campo descriptivo y evita texto libre ambiguo.
        val sexOptions = resources.getStringArray(R.array.user_sex_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSex.adapter = adapter

        val container = (application as BandAidApplication).appContainer
        val existingProfile = container.userProfileStore.getProfile()
        if (existingProfile != null) {
            binding.inputAge.setText(existingProfile.age.toString())
            binding.inputWeight.setText(existingProfile.weight.toString())
            binding.inputHeight.setText(existingProfile.height.toString())
            val index = sexOptions.indexOf(existingProfile.sex).takeIf { it >= 0 } ?: 0
            binding.spinnerSex.setSelection(index)
            binding.inputDiseases.setText(existingProfile.diseases.orEmpty())
        }

        binding.buttonSaveContinue.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        // WHY THIS DECISION:
        // Basic parsing keeps the flow simple; no medical validation is performed in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // El parseo basico mantiene el flujo simple; no hay validacion medica en v0.1.x.
        val age = binding.inputAge.text.toString().toIntOrNull() ?: 0
        val weight = binding.inputWeight.text.toString().toFloatOrNull() ?: 0f
        val height = binding.inputHeight.text.toString().toFloatOrNull() ?: 0f
        // WHY THIS DECISION:
        // The sex field is descriptive only and does not imply medical logic in v0.1.x.
        //
        // POR QUE ESTA DECISION:
        // El campo sexo es descriptivo y no implica logica medica en v0.1.x.
        val sex = binding.spinnerSex.selectedItem.toString()
        val diseases = binding.inputDiseases.text.toString().ifBlank { null }

        val profile = UserProfile(
            age = age,
            weight = weight,
            height = height,
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

        val isInitial = intent.getBooleanExtra(EXTRA_INITIAL_SETUP, false)
        if (isInitial) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    companion object {
        const val EXTRA_INITIAL_SETUP = "extra_initial_setup"
    }
}
