package com.kheemwel.mywatchlist.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC") // Set time zone to UTC
    return formatter.format(Date(millis))
}

fun convertDateToLong(dateString: String): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")// Set time zone to UTC
    val date = formatter.parse(dateString)
    return date?.time ?: 0L
}

fun getCurrentDateTimeAsString(): String {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(time)
}

fun convertDateFormat(dateString: String): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val date = inputFormatter.parse(dateString)
    return date?.let { outputFormatter.format(it) } ?: ""
}