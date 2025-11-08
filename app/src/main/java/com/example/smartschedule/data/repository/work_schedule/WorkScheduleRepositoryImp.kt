package com.example.smartschedule.data.repository.work_schedule

import android.util.Log
import com.example.smartschedule.data.mapper.WorkScheduleMapper
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.workschedule.ScheduleStatus
import com.example.smartschedule.domain.models.workschedule.WorkSchedule
import com.example.smartschedule.domain.repository.workschedule.WorkScheduleRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class WorkScheduleRepositoryImp @Inject constructor(
    private val fireStore: FirebaseFirestore
) : WorkScheduleRepository {

    private val TAG = "WorkScheduleRepository"
    private val workSchedulesCollection = fireStore.collection("workSchedules")


    override suspend fun createWorkSchedule(schedule: WorkSchedule): Result<Unit> {
        return try {
            val map = WorkScheduleMapper.toMap(schedule)
            workSchedulesCollection.document(schedule.id.toString()).set(map).await()
            Log.d(TAG, "✅ Work schedule created: ${schedule.title}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error creating work schedule", e)
            Result.Error(e)
        }
    }

    override suspend fun getWorkScheduleById(id: Long): Result<WorkSchedule?> {
        return try {
            val doc = workSchedulesCollection.document(id.toString()).get().await()
            if (!doc.exists()) {
                Log.w(TAG, "⚠️ Work schedule $id not found")
                return Result.Success(null)
            }
            val data = doc.data ?: return Result.Success(null)
            val schedule = WorkScheduleMapper.fromMap(data)
            Result.Success(schedule)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching work schedule by id", e)
            Result.Error(e)
        }
    }

    override suspend fun getAllWorkSchedules(status: ScheduleStatus?): Result<List<WorkSchedule>> {
        return try {
            val query = if (status != null) {
                workSchedulesCollection.whereEqualTo("status", status.name)
            } else {
                workSchedulesCollection
            }

            val snapshot = query.get().await()
            val schedules = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { WorkScheduleMapper.fromMap(it) }
            }

            Result.Success(schedules)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching all work schedules", e)
            Result.Error(e)
        }
    }

    override suspend fun getWorkSchedulesByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<WorkSchedule>> {
        return try {
            val start = startDate.toEpochDay()
            val end = endDate.toEpochDay()

            val snapshot = workSchedulesCollection
                .whereGreaterThanOrEqualTo("startDate", start)
                .whereLessThanOrEqualTo("endDate", end)
                .get()
                .await()

            val schedules = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { WorkScheduleMapper.fromMap(it) }
            }

            Result.Success(schedules)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching work schedules by range", e)
            Result.Error(e)
        }
    }

    override suspend fun updateWorkSchedule(schedule: WorkSchedule): Result<Unit> {
        return try {
            val map = WorkScheduleMapper.toMap(schedule)
            workSchedulesCollection.document(schedule.id.toString()).update(map).await()
            Log.d(TAG, "✅ Work schedule updated: ${schedule.id}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error updating work schedule", e)
            Result.Error(e)
        }
    }

    override suspend fun updateScheduleStatus(
        scheduleId: Long,
        newStatus: ScheduleStatus
    ): Result<Unit> {
        return try {
            workSchedulesCollection.document(scheduleId.toString())
                .update("status", newStatus.name)
                .await()
            Log.d(TAG, "✅ Schedule $scheduleId status updated to $newStatus")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error updating schedule status", e)
            Result.Error(e)
        }
    }

    override suspend fun approveWorkSchedule(
        scheduleId: Long,
        managerId: String
    ): Result<Unit> {
        return try {
            workSchedulesCollection.document(scheduleId.toString())
                .update(
                    mapOf(
                        "status" to ScheduleStatus.PUBLISHED.name,
                        "approveBy.managerId" to managerId
                    )
                )
                .await()
            Log.d(TAG, "✅ Work schedule $scheduleId approved by manager $managerId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error approving work schedule", e)
            Result.Error(e)
        }
    }

    override suspend fun deleteWorkSchedule(scheduleId: Long): Result<Unit> {
        return try {
            workSchedulesCollection.document(scheduleId.toString()).delete().await()
            Log.d(TAG, "🗑️ Work schedule deleted: $scheduleId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error deleting work schedule", e)
            Result.Error(e)
        }
    }

    override suspend fun getActiveWorkSchedule(): Result<WorkSchedule?> {
        return try {
            val snapshot = workSchedulesCollection
                .whereEqualTo("status", ScheduleStatus.PUBLISHED.name)
                .limit(1)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            val schedule = doc?.data?.let { WorkScheduleMapper.fromMap(it) }

            Result.Success(schedule)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error fetching active work schedule", e)
            Result.Error(e)
        }
    }

    override suspend fun existsInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Boolean> {
        return try {
            val start = startDate.toEpochDay()
            val end = endDate.toEpochDay()

            val snapshot = workSchedulesCollection
                .whereGreaterThanOrEqualTo("startDate", start)
                .whereLessThanOrEqualTo("endDate", end)
                .get()
                .await()

            Result.Success(snapshot.documents.isNotEmpty())
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error checking work schedule existence", e)
            Result.Error(e)
        }
    }
}