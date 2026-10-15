package com.example.mini_project.data.model

object TxType {
    const val EXPENSE = "EXPENSE"
    const val INCOME = "INCOME"
}

data class Category(
    val id: String? = null,
    val name: String,
    val emoji: String,
    val type: String,          // TxType.EXPENSE or TxType.INCOME
    val order: Int = 0         // position in the grid
) {
    fun toHashMap(): Map<String, Any> = mapOf(
        "name" to name,
        "emoji" to emoji,
        "type" to type,
        "order" to order
    )

    companion object {
        fun fromHashMap(map: Map<String, Any>): Category = Category(
            id = map["id"]?.toString(),
            name = map["name"]?.toString().orEmpty(),
            emoji = map["emoji"]?.toString().orEmpty(),
            type = map["type"]?.toString() ?: TxType.EXPENSE,
            order = (map["order"] as? Number)?.toInt() ?: 0
        )

        /** Written to Firestore once, the first time a user has no categories. */
        val defaults = listOf(
            Category("food", "Food", "🍜", TxType.EXPENSE, 1),
            Category("groceries", "Groceries", "🛒", TxType.EXPENSE, 2),
            Category("transport", "Transport", "🚌", TxType.EXPENSE, 3),
            Category("bills", "Bills", "💡", TxType.EXPENSE, 4),
            Category("fun", "Fun", "🎬", TxType.EXPENSE, 5),
            Category("health", "Health", "🩺", TxType.EXPENSE, 6),
            Category("shopping", "Shopping", "🛍️", TxType.EXPENSE, 7),
            Category("other_expense", "Other", "➕", TxType.EXPENSE, 8),
            Category("salary", "Salary", "💼", TxType.INCOME, 1),
            Category("other_income", "Other", "➕", TxType.INCOME, 2)
        )
    }
}