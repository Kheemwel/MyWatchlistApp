package com.kheemwel.mywatchlist

import com.kheemwel.mywatchlist.utils.convertDateFormat
import com.kheemwel.mywatchlist.utils.convertDateToLong
import com.kheemwel.mywatchlist.utils.convertMillisToDate
import org.junit.Assert.assertEquals
import org.junit.Test

class WatchlistTest {
    @Test
    fun longToDate_isCorrect() {
        val date = "2024-01-01"
        val convertedMillis = convertDateToLong(date)

        assertEquals(date, convertMillisToDate(convertedMillis))
    }

    @Test
    fun dateFormat_isCorrect() {
        val date = "2024-01-01"
        val convertedDate = convertDateFormat(date)
        assertEquals("January 01, 2024", convertedDate)
    }
}