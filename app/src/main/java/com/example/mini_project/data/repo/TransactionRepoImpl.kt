package com.example.mini_project.data.repo

import com.example.mini_project.core.AuthService
import com.example.mini_project.data.model.Transaction
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TransactionRepoImpl(
    private val db: FirebaseFirestore
) : TransactionRepo {

    /** Each user has their own list: users/{uid}/transactions */
    private fun collection(): CollectionReference {
        val uid = AuthService.uid ?: error("Not logged in")
        return db.collection("users").document(uid).collection("transactions")
    }

    override fun getAllTransactions(): Flow<List<Transaction>> = callbackFlow {
        val listener = collection()
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val transactions = mutableListOf<Transaction>()
                snapshots?.documents?.forEach { doc ->
                    doc.data?.let {
                        it["id"] = doc.id
                        transactions.add(Transaction.fromHashMap(it))
                    }
                }
                trySend(transactions)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        collection().add(transaction.toHashMap()).await()
    }

    override suspend fun getTransaction(id: String): Transaction? {
        val snapshot = collection().document(id).get().await()
        return snapshot.data?.let {
            it["id"] = snapshot.id
            Transaction.fromHashMap(it)
        }
    }

    override suspend fun updateTransaction(id: String, transaction: Transaction) {
        collection().document(id).set(transaction.toHashMap()).await()
    }

    override suspend fun deleteTransaction(id: String) {
        collection().document(id).delete().await()
    }
}