package com.example.mini_project.data.repo

import com.example.mini_project.core.AuthService
import com.example.mini_project.data.model.Category
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CategoryRepoImpl(
    private val db: FirebaseFirestore
) : CategoryRepo {

    private fun collection(): CollectionReference {
        val uid = AuthService.uid ?: error("Not logged in")
        return db.collection("users").document(uid).collection("categories")
    }

    override fun getCategories(): Flow<List<Category>> = callbackFlow {
        val col = collection()
        val listener = col.orderBy("order", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                if (snapshots == null) return@addSnapshotListener

                // Brand-new user: write the starter categories once.
                // The listener fires again with them after the write.
                if (snapshots.isEmpty && !snapshots.metadata.isFromCache) {
                    seedDefaults(col)
                    return@addSnapshotListener
                }

                val categories = snapshots.documents.mapNotNull { doc ->
                    doc.data?.let {
                        it["id"] = doc.id
                        Category.fromHashMap(it)
                    }
                }
                trySend(categories)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun insertCategory(category: Category) {
        collection().add(category.toHashMap()).await()
    }

    /** Fixed document ids, so running this twice can't create duplicates. */
    private fun seedDefaults(col: CollectionReference) {
        val batch = db.batch()
        Category.defaults.forEach { batch.set(col.document(it.id!!), it.toHashMap()) }
        batch.commit()
    }
}