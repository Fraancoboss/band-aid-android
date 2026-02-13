package com.bandaid.app.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bandaid.app.data.local.InMemoryMedicineMetaStore
import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.domain.model.Medicine
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

/*
 * Responsabilidad:
 * - Orquesta el estado del detalle de medicina.
 * - Gestiona acciones de usuario: registrar toma, activar/desactivar, eliminar.
 * - Emite eventos de una sola vez para feedback de UI (Snackbar).
 *
 * Capa: UI (ViewModel).
 * Alcance: flujo de detalle local para v0.1.x.
 */
class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val doseLogRepository: DoseLogRepository,
    private val calendarEntryRepository: CalendarEntryRepository,
    private val medicineMetaStore: InMemoryMedicineMetaStore
) : ViewModel() {

    private val _uiState = MutableLiveData<MedicineDetailUiState>(MedicineDetailUiState.Loading)
    val uiState: LiveData<MedicineDetailUiState> = _uiState

    private val _event = MutableLiveData<SingleEvent<DetailEvent>>()
    val event: LiveData<SingleEvent<DetailEvent>> = _event

    private var currentMedicineId: String? = null

    private val doseLogFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm", Locale.getDefault()
    )

    fun loadMedicine(id: String) {
        currentMedicineId = id
        val medicine = medicineRepository.getById(id)
        if (medicine == null) {
            _uiState.value = MedicineDetailUiState.NotFound
            return
        }
        val logs = doseLogRepository.getAll()
            .filter { it.medicineId == id }
            .sortedByDescending { it.takenAt }
        val duration = medicineMetaStore.getDuration(id)
        _uiState.value = MedicineDetailUiState.Content(
            MedicineDetailUiModel(
                medicine = medicine,
                duration = duration,
                doseLogs = logs
            )
        )
    }

    fun registerDose() {
        val medicineId = currentMedicineId ?: return
        val takenAt = LocalDateTime.now()
        val scheduledTime = resolveScheduledTime(medicineId, takenAt)
        val doseLog = DoseLog(
            id = UUID.randomUUID().toString(),
            medicineId = medicineId,
            scheduledTime = scheduledTime,
            takenAt = takenAt,
            status = "taken",
            notes = null
        )
        doseLogRepository.upsert(doseLog)
        _event.value = SingleEvent(DetailEvent.DoseRegistered)
        loadMedicine(medicineId)
    }

    fun deleteMedicine() {
        val medicineId = currentMedicineId ?: return
        medicineRepository.deleteById(medicineId)
        _uiState.value = MedicineDetailUiState.Deleted
    }

    fun toggleActive() {
        val medicineId = currentMedicineId ?: return
        val medicine = medicineRepository.getById(medicineId) ?: return
        val updated = medicine.copy(isActive = !medicine.isActive)
        medicineRepository.upsert(updated)
        if (updated.isActive) {
            _event.value = SingleEvent(DetailEvent.Activated)
        } else {
            _event.value = SingleEvent(DetailEvent.Deactivated)
        }
        loadMedicine(medicineId)
    }

    private fun resolveScheduledTime(medicineId: String, takenAt: LocalDateTime): LocalDateTime? {
        val entries = calendarEntryRepository.getAll()
            .filter { it.medicineId == medicineId }
            .filter { it.expectedAt <= takenAt }
        if (entries.isEmpty()) return null
        return entries.maxBy { it.expectedAt }.expectedAt
    }

    fun formatDoseLog(log: DoseLog): String =
        log.takenAt.format(doseLogFormatter)
}

/*
 * Estado de pantalla para detalle de medicina.
 */
sealed class MedicineDetailUiState {
    object Loading : MedicineDetailUiState()
    object NotFound : MedicineDetailUiState()
    data class Content(val model: MedicineDetailUiModel) : MedicineDetailUiState()
    object Deleted : MedicineDetailUiState()
}

/*
 * DTO de presentacion para pintar la vista de detalle.
 */
data class MedicineDetailUiModel(
    val medicine: Medicine,
    val duration: String?,
    val doseLogs: List<DoseLog>
)

/*
 * Eventos puntuales de UI para mensajes al usuario.
 */
sealed class DetailEvent {
    object DoseRegistered : DetailEvent()
    object Deactivated : DetailEvent()
    object Activated : DetailEvent()
}

/*
 * Wrapper simple para consumir un evento una sola vez.
 */
class SingleEvent<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
