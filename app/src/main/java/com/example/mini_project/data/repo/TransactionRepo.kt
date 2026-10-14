package com.example.mini_project.data.repo

import com.example.mini_project.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepo {
    fun getAllTransactions(): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun getTransaction(id: String): Transaction?

    suspend fun updateTransaction(id: String, transaction: Transaction)

    suspend fun deleteTransaction(id: String)
}