package com.example.mini_project.data.model

import com.example.mini_project.core.toLocalDate
import com.google.firebase.Timestamp
import java.time.LocalDate

data class Transaction(
    val id: String? = null,
    val amount: Double,
    val type: String,             // TxType.EXPENSE or TxType.INCOME
    val categoryId: String,
    val categoryName: String,     // copied from the category when saved
    val categoryEmoji: String,
    val date: Timestamp,
    val paidWith: String = "Cash",
    val note: String = ""
) {
    val isExpense: Boolean get() = type == TxType.EXPENSE

    val localDate: LocalDate get() = date.toLocalDate()

    fun toHashMap(): Map<String, Any> = mapOf(
        "amount" to amount,
        "type" to type,
        "categoryId" to categoryId,
        "categoryName" to categoryName,
        "categoryEmoji" to categoryEmoji,
        "date" to date,
        "paidWith" to paidWith,
        "note" to note
    )

    companion object {
        fun fromHashMap(map: Map<String, Any>): Transaction {
            // Older documents only had "category" (e.g. "FOOD"); still show them
            val old = map["category"]?.toString()
            return Transaction(
                id = map["id"]?.toString(),
                amount = (map["amount"] as? Number)?.toDouble() ?: 0.0,
                type = map["type"]?.toString() ?: TxType.EXPENSE,
                categoryId = map["categoryId"]?.toString() ?: old?.lowercase() ?: "",
                categoryName = map["categoryName"]?.toString()
                    ?: old?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Other",
                categoryEmoji = map["categoryEmoji"]?.toString() ?: "💸",
                date = map["date"] as? Timestamp ?: Timestamp.now(),
                paidWith = map["paidWith"]?.toString().orEmpty(),
                note = map["note"]?.toString().orEmpty()
            )
        }
    }
}