package com.example.smartschedule.core.data.firebase.utils

import android.util.Log
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


/**
 * Observes a Firestore collection for real-time updates and transforms the documents into a list of objects of type [T].
 *
 * This extension function attaches a snapshot listener to a [CollectionReference]. Whenever the data in the
 * collection changes, it attempts to convert each document snapshot into an object of the specified class [T].
 * The resulting list of objects is then emitted to the returned [Flow].
 *
 * The listener is automatically removed when the collecting flow is cancelled.
 *
 * @param T The type of the data model class to which the Firestore documents will be converted.
 * @return A [Flow] that emits a `List<T>` whenever the collection's data changes.
 *         The flow will emit an empty list if the collection is empty or if conversion fails for all documents.
 */
inline fun <reified T, R> Query.observeCollection(
    crossinline mapper: (T) -> R
): Flow<List<R>> = callbackFlow {
    val registration = addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
        if (error != null) {
            Log.e("FirestoreExt", "Error observing collection", error)
            close(error)
            return@addSnapshotListener
        }

        if (snapshot != null) {
            val domainList = try {
                snapshot.documents.mapNotNull { doc ->
                    // המרה ל-DTO ואז ל-Domain באמצעות ה-Mapper
                    doc.toObject(T::class.java)?.let(mapper)
                }
            } catch (e: Exception) {
                Log.e("FirestoreExt", "Mapping error in collection", e)
                emptyList()
            }
            trySend(domainList)
        }
    }

    awaitClose { registration.remove() }
}


/**
 * Observes a single Firestore document for real-time updates.
 *
 * This extension function attaches a snapshot listener to a [DocumentReference]. It provides a cold [Flow]
 * that emits the document's data as a `Result<T>` whenever the data changes. The data is
 * deserialized into the specified type [T].
 *
 * The flow will emit `Result.success(data)` if the document exists and deserialization is successful.
 * If the document does not exist, it will emit `Result.failure` with a `NoSuchElementException`.
 * If any other Firestore-related error occurs (e.g., permission denied), it will also emit `Result.failure`.
 *
 * The listener is automatically removed when the collecting coroutine is cancelled.
 *
 * @param T The type to which the document data should be deserialized. The class must be a valid
 *          data model for Firestore deserialization (e.g., a data class with an empty constructor).
 * @return A [Flow] that emits a `Result<T>` containing the deserialized document data or an exception.
 * @see com.google.firebase.firestore.DocumentReference.addSnapshotListener
 * @see kotlinx.coroutines.channels.awaitClose
 */
inline fun <reified T, R> DocumentReference.observeDocument(
    crossinline mapper: (T) -> R
): Flow<R> = callbackFlow {
    val registration = addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
        if (error != null) {
            Log.e("FirestoreExt", "Error observing document", error)
            close(error)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            try {
                val dto = snapshot.toObject(T::class.java)
                if (dto != null) {
                    trySend(mapper(dto))
                }
            } catch (e: Exception) {
                Log.e("FirestoreExt", "Mapping error in document", e)
            }
        } else {
            Log.d("FirestoreExt", "Document does not exist or was deleted")
        }
    }
    awaitClose { registration.remove() }
}