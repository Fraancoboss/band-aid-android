package com.bandaid.app.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
 * Responsabilidad:
 * - Prepara el estado del calendario diario para la UI.
 * - Cruza CalendarEntry + DoseLog para marcar pendiente/tomada.
 * - Gestiona navegacion de fechas (anterior/siguiente/hoy).
 *
 * Capa: UI (ViewModel).
 * Alcance: lectura y transformacion de datos para v0.1.x.
 */
class CalendarViewModel(
    private val calendarEntryRepository: CalendarEntryRepository,
    private val doseLogRepository: DoseLogRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<CalendarUiState>(CalendarUiState.Loading)
    val uiState: LiveData<CalendarUiState> = _uiState

    private var selectedDate: LocalDate = LocalDate.now()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(
        "HH:mm", Locale.getDefault()
    )

    init {
        load()
    }

    fun load() {
        val entries = calendarEntryRepository.getAll()
            .filter { it.expectedAt.toLocalDate() == selectedDate }
            .sortedBy { it.expectedAt }

        if (entries.isEmpty()) {
            _uiState.value = CalendarUiState.Empty(selectedDate)
            return
        }

        val doseLogs = doseLogRepository.getAll()
        val models = entries.map { entry ->
            val medicineName = medicineRepository.getById(entry.medicineId)?.name ?: "?"
            val isTaken = doseLogs.any {
                it.medicineId == entry.medicineId &&
                    it.scheduledTime == entry.expectedAt
            }
            CalendarEntryUiModel(
                entryId = entry.id,
                medicineId = entry.medicineId,
                medicineName = medicineName,
                time = entry.expectedAt.format(dateTimeFormatter),
                isTaken = isTaken
            )
        }
        _uiState.value = CalendarUiState.Content(selectedDate, models)
    }

    fun previousDay() {
        selectedDate = selectedDate.minusDays(1)
        load()
    }

    fun nextDay() {
        selectedDate = selectedDate.plusDays(1)
        load()
    }

    fun goToToday() {
        selectedDate = LocalDate.now()
        load()
    }
}

/*
 * Estado de pantalla para CalendarActivity.
 * Loading: carga inicial.
 * Empty: no hay entradas para la fecha seleccionada.
 * Content: hay entradas renderizables.
 */
sealed class CalendarUiState {
    object Loading : CalendarUiState()
    data class Empty(val date: LocalDate) : CalendarUiState()
    data class Content(val date: LocalDate, val entries: List<CalendarEntryUiModel>) : CalendarUiState()
}

/*
 * Modelo de presentacion de una fila del calendario.
 * Ya incluye datos listos para pintar en la vista.
 */
data class CalendarEntryUiModel(
    val entryId: String,
    val medicineId: String,
    val medicineName: String,
    val time: String,
    val isTaken: Boolean
)
