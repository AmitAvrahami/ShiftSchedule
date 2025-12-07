package com.example.smartschedule.feature.smartSchedule.data.repository

import android.util.Log
import com.example.smartschedule.core.data.firebase.constants.Collections
import com.example.smartschedule.core.data.firebase.model.ConstraintDto
import com.example.smartschedule.core.data.firebase.model.ScheduleDto
import com.example.smartschedule.core.data.firebase.model.UserDto
import com.example.smartschedule.core.data.firebase.model.WeeklyRuleDto
import com.example.smartschedule.core.data.firebase.utils.observeCollection
import com.example.smartschedule.core.data.firebase.utils.observeDocument
import com.example.smartschedule.core.data.mapper.toDomain
import com.example.smartschedule.core.data.mapper.toDto
import com.example.smartschedule.core.domain.model.constraints.Constraint
import com.example.smartschedule.core.domain.model.constraints.WeeklyRule
import com.example.smartschedule.core.domain.model.employees.Employee
import com.example.smartschedule.core.domain.model.smartSchedule.WorkSchedule
import com.example.smartschedule.core.domain.time.convertLongToIsoDateString
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
    private val firestore : FirebaseFirestore
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
    override fun getWorkSchedule(scheduleId: String): Flow<WorkSchedule> {
        return firestore.collection(Collections.WORK_SCHEDULES.path)
            .document(scheduleId)
            .observeDocument<ScheduleDto, WorkSchedule> { dto ->
                dto.toDomain()
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
    override fun getEmployees(): Flow<List<Employee>> {
        return firestore.collection(Collections.USERS.path)
            .observeCollection<UserDto, Employee> { dto ->
                dto.toDomain()
            }
    }

    /**
     * Retrieves a real-time stream of constraints that are active within a given time period.
     *
     * This function queries the Firestore 'constraints' collection for documents whose 'endDate'
     * is on or after the specified `start` date. This ensures that all potentially relevant
     * constraints for the period are fetched. The results are then mapped from `ConstraintDto`
     * data transfer objects to `Constraint` domain models.
     *
     * Note: The current implementation uses a `callbackFlow` but only sets up a query that
     * leverages the `observeCollection` utility. The `end` parameter is not currently used in the
     * Firestore query but is intended for future filtering logic.
     *
     * @param start The start of the time period as a Unix timestamp (in milliseconds).
     * @param end The end of the time period as a Unix timestamp (in milliseconds).
     * @return A [Flow] that emits a list of `Constraint` domain models. The flow will update
     *         in real-time as constraints in Firestore are added, modified, or removed.
     */
    override fun getConstraints(
        start: Long,
        end: Long
    ): Flow<List<Constraint>> {

        val periodStartString = start.convertLongToIsoDateString()

        return firestore.collection(Collections.CONSTRAINTS.path)
            .whereGreaterThanOrEqualTo("endDate", periodStartString)
            .observeCollection<ConstraintDto, Constraint> { dto ->
                dto.toDomain()
            }
    }

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

            firestore.collection(Collections.WORK_SCHEDULES.path)
                .document(scheduleDto.scheduleId)
                .set(scheduleDto)
                .await()

            Result.success(Unit)
        } catch (e : Exception) {
            Log.e(
                TAG ,
                "Error updating schedule" ,
                e
            )
            Result.failure(e)
        }
    }

    /**
     * Retrieves a real-time stream of all weekly rules from Firestore.
     *
     * This function is intended to set up a snapshot listener on the 'weekly_rules' collection
     * in Firestore. It would then map the documents to `WeeklyRule` domain objects and emit
     * the complete list whenever the data changes. The listener would be automatically removed
     * when the collecting coroutine is cancelled.
     *
     * NOTE: The current implementation is a placeholder and immediately closes the flow.
     * It needs to be implemented to actually fetch data from Firestore.
     *
     * @return A [Flow] that is intended to emit a list of `WeeklyRule` objects.
     */
    override fun getWeeklyRules(): Flow<List<WeeklyRule>> {
        return firestore.collection(Collections.WEEKLY_RULES.path)
            .observeCollection<WeeklyRuleDto , WeeklyRule> { dto ->
                dto.toDomain()
            }
    }}