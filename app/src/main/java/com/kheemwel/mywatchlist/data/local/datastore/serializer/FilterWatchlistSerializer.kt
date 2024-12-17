package com.kheemwel.mywatchlist.data.local.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.kheemwel.mywatchlist.FilterWatchlist
import java.io.InputStream
import java.io.OutputStream

object FilterWatchlistSerializer : Serializer<FilterWatchlist> {
    override val defaultValue: FilterWatchlist = FilterWatchlist.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FilterWatchlist {
        return try {
            FilterWatchlist.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: FilterWatchlist, output: OutputStream) {
        t.writeTo(output)
    }
}