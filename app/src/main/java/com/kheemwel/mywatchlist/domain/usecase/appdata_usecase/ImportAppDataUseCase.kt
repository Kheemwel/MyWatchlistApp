package com.kheemwel.mywatchlist.domain.usecase.appdata_usecase

import com.kheemwel.mywatchlist.domain.model.AppData
import com.kheemwel.mywatchlist.domain.model.DataAction
import com.kheemwel.mywatchlist.domain.repository.AppDataRepository
import javax.inject.Inject

class ImportAppDataUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    suspend operator fun invoke(appData: AppData, action: DataAction) {
        repository.importData(appData, action)
    }
}