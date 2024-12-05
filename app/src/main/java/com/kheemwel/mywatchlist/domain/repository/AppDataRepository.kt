package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.AppData
import com.kheemwel.mywatchlist.domain.model.DataAction

interface AppDataRepository {
    suspend fun exportData(): AppData
    suspend fun importData(appData: AppData, action: DataAction)
}