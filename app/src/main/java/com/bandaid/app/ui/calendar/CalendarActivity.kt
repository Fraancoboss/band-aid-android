/*
 * Responsibility:
 * - Displays read-only calendar entries from CalendarViewModel.
 * - Supports day navigation and empty/content rendering.
 * - Does NOT modify schedules or persist data.
 * Layer: UI (Activity).
 * Scope: stable for v0.1.x.
 */
package com.bandaid.app.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityCalendarBinding
import com.bandaid.app.databinding.ItemCalendarEntryBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var viewModel: CalendarViewModel
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val container = (application as BandAidApplication).appContainer
        viewModel = ViewModelProvider(
            this,
            container.calendarViewModelFactory
        )[CalendarViewModel::class.java]

        binding.buttonPrevDay.setOnClickListener { viewModel.previousDay() }
        binding.buttonNextDay.setOnClickListener { viewModel.nextDay() }
        binding.buttonToday.setOnClickListener { viewModel.goToToday() }

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is CalendarUiState.Loading -> Unit
                is CalendarUiState.Empty -> renderEmpty(state.date)
                is CalendarUiState.Content -> renderContent(state.date, state.entries)
            }
        }
    }

    private fun renderEmpty(date: LocalDate) {
        updateSelectedDateLabel(date)
        binding.layoutCalendarEntries.removeAllViews()
        binding.layoutCalendarEmpty.visibility = View.VISIBLE
        binding.scrollEntries.visibility = View.GONE
    }

    private fun renderContent(date: LocalDate, entries: List<CalendarEntryUiModel>) {
        updateSelectedDateLabel(date)
        binding.layoutCalendarEntries.removeAllViews()
        binding.layoutCalendarEmpty.visibility = View.GONE
        binding.scrollEntries.visibility = View.VISIBLE

        entries.forEach { entry ->
            val itemBinding = ItemCalendarEntryBinding.inflate(
                layoutInflater,
                binding.layoutCalendarEntries,
                false
            )
            itemBinding.textMedicine.text = getString(
                R.string.calendar_medicine_format,
                entry.medicineName
            )
            itemBinding.textExpectedAt.text = getString(
                R.string.calendar_expected_at_format,
                entry.time
            )
            val statusText = if (entry.isTaken) {
                getString(R.string.calendar_status_taken)
            } else {
                getString(R.string.calendar_status_pending)
            }
            itemBinding.textStatus.text = getString(
                R.string.calendar_status_format,
                statusText
            )
            val statusColor = if (entry.isTaken) {
                ContextCompat.getColor(this, R.color.status_taken)
            } else {
                ContextCompat.getColor(this, R.color.status_pending)
            }
            itemBinding.viewStatusIndicator.setBackgroundColor(statusColor)
            binding.layoutCalendarEntries.addView(itemBinding.root)
        }
    }

    private fun updateSelectedDateLabel(date: LocalDate) {
        binding.textSelectedDate.text = getString(
            R.string.calendar_selected_date_format,
            date.format(dateFormatter)
        )
    }
}
