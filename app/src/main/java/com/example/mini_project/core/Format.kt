package com.example.mini_project.core

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Format {
    private val fullDate = DateTimeFormatter.ofPattern("EEE, d MMM yyyy")
    private val shortDate = DateTimeFormatter.ofPattern("d MMM")

    fun money(value: Double): String = String.format(Locale.US, "$%,.2f", value)

    /** 14.0 -> "14", 14.5 -> "14.5" (for putting back into the amount box) */
    fun plainAmount(value: Double): String = value.toBigDecimal().stripTrailingZeros().toPlainString()

    fun fullDate(date: LocalDate): String = date.format(fullDate)

    fun shortDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> "Today"
            today.minusDays(1) -> "Yesterday"
            else -> date.format(shortDate)
        }
    }
}

fun Timestamp.toLocalDate(): LocalDate =
    toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toTimestamp(): Timestamp =
    Timestamp(Date.from(atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant()))