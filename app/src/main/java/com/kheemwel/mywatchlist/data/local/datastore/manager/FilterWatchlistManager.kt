package com.kheemwel.mywatchlist.data.local.datastore.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.kheemwel.mywatchlist.FilterWatchlist
import com.kheemwel.mywatchlist.data.local.datastore.serializer.FilterWatchlistSerializer
import kotlinx.coroutines.flow.Flow

private val Context.filterWatchlistDataStore: DataStore<FilterWatchlist> by dataStore(
    fileName = "filter_watch_list.pb",
    serializer = FilterWatchlistSerializer
)

class FilterWatchlistManager(private val context: Context) {
    private val dataStore = context.filterWatchlistDataStore

    fun getFilterWatchlist(): Flow<FilterWatchlist> = dataStore.data

    suspend fun updateFilterWatchlist(filterWatchlist: FilterWatchlist) {
        dataStore.updateData {
            filterWatchlist
        }
    }
}