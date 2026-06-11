package com.example.data

import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val medicationDao: MedicationDao) {
    val allMedications: Flow<List<Medication>> = medicationDao.getAllMedications()

    suspend fun getMedicationById(id: Int) = medicationDao.getMedicationById(id)
    suspend fun insert(medication: Medication) = medicationDao.insertMedication(medication)
    suspend fun update(medication: Medication) = medicationDao.updateMedication(medication)
    suspend fun delete(medication: Medication) = medicationDao.deleteMedication(medication)
}
