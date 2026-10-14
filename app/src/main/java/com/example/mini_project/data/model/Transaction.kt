package com.example.mini_project.data.model

import com.google.firebase.Timestamp

data class Transaction(
    val id: String? = null,
    val amount: Double,
    val type: String,          // "EXPENSE" or "INCOME"
    val category: String,      // e.g. "FOOD"
    val date: Timestamp,
    val paidWith: String = "Cash",
    val note: String = ""
) {
    val isExpense: Boolean get() = type == "EXPENSE"

    fun toHashMap(): Map<String, Any> = mapOf(
        "amount" to amount,
        "type" to type,
        "category" to category,
        "date" to date,
        "paidWith" to paidWith,
        "note" to note
    )

    companion object {
        fun fromHashMap(map: Map<String, Any>): Transaction = Transaction(
            id = map["id"]?.toString(),
            amount = (map["amount"] as? Number)?.toDouble() ?: 0.0,
            type = map["type"]?.toString() ?: "EXPENSE",
            category = map["category"]?.toString() ?: "OTHER",
            date = map["date"] as? Timestamp ?: Timestamp.now(),
            paidWith = map["paidWith"]?.toString().orEmpty(),
            note = map["note"]?.toString().orEmpty()
        )
    }
}