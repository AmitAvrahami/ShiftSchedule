package com.example.smartschedule.data.repository.constraint

import android.util.Log
import com.example.smartschedule.data.mapper.ConstraintMapper
import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.constraint.Constraint
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ConstraintRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ConstraintRepository {

    private val TAG = "ConstraintRepository"
    private val constraintsCollection = fireStore.collection("constraints")

    override suspend fun createConstraint(constraint: Constraint): Result<Unit> {
        return try {
            val map = ConstraintMapper.toMap(constraint)
            constraintsCollection.document(constraint.id.toString()).set(map).await()
            Log.d(TAG, "✅ Constraint created: ${constraint.id}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error creating constraint", e)
            Result.Error(e)
        }
    }

    override suspend fun getConstraintById(id: Long): Result<Constraint?> {
        return try {
            val doc = constraintsCollection.document(id.toString()).get().await()
            if (!doc.exists()) return Result.Success(null)
            val data = doc.data ?: return Result.Success(null)
            Result.Success(ConstraintMapper.fromMap(data))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAllConstraints(): Result<List<Constraint>> {
        return try {
            val snapshot = constraintsCollection.get().await()
            val list = snapshot.documents.mapNotNull { it.data?.let { ConstraintMapper.fromMap(it) } }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getConstraintsByEmployee(employeeId: String): Result<List<Constraint>> {
        return try {
            val snapshot = constraintsCollection
                .whereEqualTo("employee.id", employeeId)
                .get().await()
            val list = snapshot.documents.mapNotNull { it.data?.let { ConstraintMapper.fromMap(it) } }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getActiveConstraints(): Result<List<Constraint>> {
        return try {
            val snapshot = constraintsCollection.whereEqualTo("isActive", true).get().await()
            val list = snapshot.documents.mapNotNull { it.data?.let { ConstraintMapper.fromMap(it) } }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getConstraintsByDateRange(startDate: LocalDate, endDate: LocalDate): Result<List<Constraint>> {
        return try {
            val snapshot = constraintsCollection
                .whereGreaterThanOrEqualTo("startDate", startDate.toEpochDay())
                .whereLessThanOrEqualTo("endDate", endDate.toEpochDay())
                .get().await()
            val list = snapshot.documents.mapNotNull { it.data?.let { ConstraintMapper.fromMap(it) } }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateConstraint(constraint: Constraint): Result<Unit> {
        return try {
            val map = ConstraintMapper.toMap(constraint)
            constraintsCollection.document(constraint.id.toString()).update(map).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun setConstraintActive(id: Long, isActive: Boolean): Result<Unit> {
        return try {
            constraintsCollection.document(id.toString()).update("isActive", isActive).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteConstraint(id: Long): Result<Unit> {
        return try {
            constraintsCollection.document(id.toString()).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
