package com.example.smartschedule.data.repository.shift

import android.util.Log
import com.example.smartschedule.data.mapper.ShiftMapper
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.shift.Shift
import com.example.smartschedule.domain.repository.shift.ShiftRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ShiftRepository {

    private val TAG = "ShiftRepository"
    private val shiftsCollection = fireStore.collection("shifts")

    override suspend fun createShift(shift: Shift): Result<Unit> {
        return try {
            val map = ShiftMapper.toMap(shift)
            shiftsCollection.document(shift.id.toString()).set(map).await()
            Log.d(TAG, "✅ Created shift: ${shift.id}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error creating shift", e)
            Result.Error(e)
        }
    }

    override suspend fun getShiftById(id: Long): Result<Shift?> {
        return try {
            val doc = shiftsCollection.document(id.toString()).get().await()
            if (!doc.exists()) return Result.Success(null)
            val shift = ShiftMapper.fromMap(doc.data!!)
            Result.Success(shift)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching shift by id", e)
            Result.Error(e)
        }
    }

    override suspend fun getAllShifts(): Result<List<Shift>> {
        return try {
            val snapshot = shiftsCollection.get().await()
            val shifts = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { ShiftMapper.fromMap(it) }
            }
            Result.Success(shifts)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching shifts", e)
            Result.Error(e)
        }
    }

    override suspend fun getShiftsByType(type: String): Result<List<Shift>> {
        return try {
            val snapshot = shiftsCollection
                .whereEqualTo("strategyType", type.uppercase())
                .get()
                .await()
            val shifts = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { ShiftMapper.fromMap(it) }
            }
            Result.Success(shifts)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching shifts by type", e)
            Result.Error(e)
        }
    }

    override suspend fun getShiftsByDate(date: LocalDate): Result<List<Shift>> {
        return try {
            val epochDay = date.toEpochDay()
            val snapshot = shiftsCollection
                .whereEqualTo("date", epochDay)
                .get()
                .await()
            val shifts = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { ShiftMapper.fromMap(it) }
            }
            Result.Success(shifts)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching shifts by date", e)
            Result.Error(e)
        }
    }

    override suspend fun updateShift(shift: Shift): Result<Unit> {
        return try {
            val map = ShiftMapper.toMap(shift)
            shiftsCollection.document(shift.id.toString()).update(map).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteShift(id: Long): Result<Unit> {
        return try {
            shiftsCollection.document(id.toString()).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
