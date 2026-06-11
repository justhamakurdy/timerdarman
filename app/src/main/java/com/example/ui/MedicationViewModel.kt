package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MedicationRepository
    private val scheduler: AlarmScheduler
    val allMedications: StateFlow<List<Medication>>

    init {
        val dao = MedicationDatabase.getDatabase(application).medicationDao()
        repository = MedicationRepository(dao)
        scheduler = AlarmScheduler(application)
        allMedications = repository.allMedications.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addMedication(name: String, dosage: String, hour: Int, minute: Int) {
        viewModelScope.launch {
            val medication = Medication(
                name = name,
                dosage = dosage,
                alarmHour = hour,
                alarmMinute = minute
            )
            val id = repository.insert(medication)
            scheduler.schedule(medication.copy(id = id.toInt()))
        }
    }

    fun updateMedication(id: Int, name: String, dosage: String, hour: Int, minute: Int, isActive: Boolean) {
        viewModelScope.launch {
            val updated = Medication(
                id = id,
                name = name,
                dosage = dosage,
                alarmHour = hour,
                alarmMinute = minute,
                isActive = isActive
            )
            repository.update(updated)
            if (updated.isActive) {
                scheduler.schedule(updated)
            } else {
                scheduler.cancel(updated)
            }
        }
    }

    suspend fun getMedicationById(id: Int): Medication? {
        return repository.getMedicationById(id)
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            repository.delete(medication)
            scheduler.cancel(medication)
        }
    }

    fun toggleMedication(medication: Medication) {
        viewModelScope.launch {
            val updated = medication.copy(isActive = !medication.isActive)
            repository.update(updated)
            if (updated.isActive) {
                scheduler.schedule(updated)
            } else {
                scheduler.cancel(updated)
            }
        }
    }
}
