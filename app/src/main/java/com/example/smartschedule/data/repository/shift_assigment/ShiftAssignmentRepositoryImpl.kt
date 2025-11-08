package com.example.smartschedule.data.repository.shift_assigment

import com.example.smartschedule.data.repository.Result
import com.example.smartschedule.domain.models.shiftassignment.Assignment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShiftAssignmentRepositoryImpl @Inject constructor(
    fireStore: FirebaseFirestore
): ShiftAssignmentRepository {

    private val assignmentsCollection = fireStore.collection("assignments")

    override suspend fun getNextAssignmentByUserId(userId : String) : Result<Assignment?> {
        return try{
            val snapshot = assignmentsCollection
                .whereArrayContains("assignedEmployees",userId)
                .whereGreaterThan("shift.startTime",System.currentTimeMillis())
                .orderBy("shift.startTime")
                .limit(1)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()
            val assignment = document?.toObject(Assignment::class.java)
            Result.Success(assignment)

        }catch (e: Exception) {
            Result.Error(e)
        }
    }
}