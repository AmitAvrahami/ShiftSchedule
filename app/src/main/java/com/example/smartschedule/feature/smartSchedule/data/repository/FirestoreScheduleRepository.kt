package com.example.smartschedule.feature.smartSchedule.data.repository

import android.system.Os.close
import android.util.Log
import com.example.smartschedule.core.data.firebase.constants.Collections
import com.example.smartschedule.core.data.firebase.model.ScheduleDto
import com.example.smartschedule.core.data.firebase.model.UserDto
import com.example.smartschedule.core.data.mapper.toDomain
import com.example.smartschedule.core.data.mapper.toDto
import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.feature.smartSchedule.domain.repository.ScheduleRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreScheduleRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : ScheduleRepository {

    companion object {
        private const val TAG = "FirestoreScheduleRepository"
    }

    /**
     * Retrieves a specific work schedule from Firestore and listens for real-time updates.
     *
     * This function sets up a snapshot listener on a specific work schedule document
     * in the Firestore database. It uses a `callbackFlow` to emit the `WorkSchedule`
     * object whenever the document data changes.
     *
     * The flow will emit a new `WorkSchedule` item upon initial retrieval and for every
     * subsequent update to the document. If the document is deleted or doesn't exist,
     * no item will be emitted.
     *
     * The listener is automatically removed when the collecting coroutine is cancelled,
     * preventing resource leaks.
     *
     * @param scheduleId The unique identifier of the work schedule document to retrieve.
     * @return A [Flow] that emits the `WorkSchedule` domain model. The flow will close
     *         with an error if the listener fails or if there's a problem deserializing the data.
     */
    override fun getWorkSchedule(scheduleId : String) : Flow<WorkSchedule> = callbackFlow {
        val docRef = firestore.collection(Collections.WORK_SCHEDULES).document(scheduleId)
        val listenerRegistration = docRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot , error ->
            if (error != null) {
                Log.e(
                    TAG,
                    "Listen failed.",
                    error
                )
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                try {
                    val dto = snapshot.toObject(ScheduleDto::class.java)
                    if (dto != null) {
                        val domainSchedule = dto.toDomain()
                        trySend(domainSchedule)
                        Log.d(
                            TAG,
                            "Schedule update emitted"
                        )
                    }
                } catch (e : Exception) {
                    Log.e(
                        TAG,
                        "Data mapping error",
                        e
                    )
                }
            } else {
                Log.d(
                    TAG,
                    "Current data: null (Document does not exist)"
                )
            }
        }
        awaitClose {
            Log.d(
                TAG,
                "Closing listener"
            )
            listenerRegistration.remove()
        }
    }


    /**
     * Retrieves a real-time stream of all employees from the Firestore 'users' collection.
     *
     * This function sets up a snapshot listener on the 'users' collection. Whenever the
     * data in this collection changes, it maps the documents to `Employee` domain objects
     * and emits the complete list of employees as a `Flow`.
     *
     * Any errors during the fetch or mapping process are logged, and malformed documents are ignored.
     * The listener is automatically removed when the collecting coroutine is cancelled.
     *
     * @return A `Flow` that emits a list of `Employee` objects on every update from Firestore.
     *         The flow will be closed with an exception if a listener error occurs.
     */
    override fun getEmployees() : Flow<List<Employee>>  = callbackFlow {
        val collectionRef = firestore.collection(Collections.USERS)
        val registration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val employees = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(UserDto::class.java)?.toDomain()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error mapping user: ${doc.id}", e)
                        null
                    }
                }
                trySend(employees)
            }
        }
        awaitClose { registration.remove() }
    }

    override fun getConstraints(
        start : Long ,
        end : Long
    ) : Flow<List<Constraint>> = callbackFlow { close() }


    override fun getWeeklyRules() : Flow<List<WeeklyRule>> = callbackFlow { close() }

    /**
     * Updates an existing work schedule in the Firestore database.
     *
     * This function takes a [WorkSchedule] domain model, converts it into a data transfer object (DTO),
     * and then overwrites the corresponding document in the Firestore `WORK_SCHEDULES` collection.
     *
     * @param schedule The [WorkSchedule] object containing the updated data. The ID of this object
     *                 is used to identify the document to update.
     * @return A [Result] object that is [Result.success] with `Unit` if the update is successful,
     *         or [Result.failure] with an exception if the operation fails.
     */
    override suspend fun updateSchedule(schedule : WorkSchedule) : Result<Unit> {
        return try {
            val scheduleDto = schedule.toDto()

            firestore.collection(Collections.WORK_SCHEDULES)
                .document(scheduleDto.scheduleId)
                .set(scheduleDto) 
                .await() 

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating schedule", e)
            Result.failure(e)
        }
    }
}