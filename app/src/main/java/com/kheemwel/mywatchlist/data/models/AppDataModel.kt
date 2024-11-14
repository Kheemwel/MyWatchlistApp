package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Serializable
data class AppData(
    val statuses: List<String>,
    val genres: List<String>,
    val countries: List<String>,
    val movies: List<Movie>,
    val series: List<Series>
)

class AppDataModel(
    private val statusModel: StatusModel,
    private val genreModel: GenreModel,
    private val countryModel: CountryModel,
    private val movieModel: MovieModel,
    private val seriesModel: SeriesModel
) : ViewModel() {
    private val statuses = statusModel.statuses
    private val genres = genreModel.genres
    private val countries = countryModel.countries
    private val movies = movieModel.movies
    private val series = seriesModel.series

    fun getAppData(): AppData {
        return AppData(statuses.value, genres.value, countries.value, movies.value, series.value)
    }

    fun getAppDataJson(): String {
        return Json.encodeToString(getAppData())
    }

    fun saveAppData(appData: AppData, action: DataAction) {
        statusModel.saveStatuses(appData.statuses, action)
        genreModel.saveGenres(appData.genres, action)
        countryModel.saveCountries(appData.countries, action)
        movieModel.saveMovies(appData.movies, action)
        seriesModel.saveSeries(appData.series, action)
    }

    fun saveAppDataJson(appDataJson: String, action: DataAction) {
        val appData = Json.decodeFromString<AppData>(appDataJson)
        saveAppData(appData, action)
    }

    suspend fun encodeAppDataToJsonWithProgress(onProgressUpdate: (Float) -> Unit): String {
        val appData = getAppData()
        return withContext(Dispatchers.Default) {
            val totalItems =
                appData.statuses.size + appData.genres.size + appData.countries.size + appData.movies.size + appData.series.size
            var processedItems = 0f

            val jsonString = suspendCoroutine { continuation ->
                val json = StringBuilder()
                json.append("{")

                // Encode statuses
                json.append("\"statuses\":[")
                appData.statuses.forEachIndexed { index, statuses ->
                    json.append(Json.encodeToString(statuses))
                    if (index < appData.statuses.size - 1) {
                        json.append(",")
                    }
                    processedItems++
                    onProgressUpdate((processedItems * 100) / totalItems) // Update progress
                }
                json.append("],")

                // Encode genres
                json.append("\"genres\":[")
                appData.genres.forEachIndexed { index, genres ->
                    json.append(Json.encodeToString(genres))
                    if (index < appData.genres.size - 1) {
                        json.append(",")
                    }
                    processedItems++
                    onProgressUpdate((processedItems * 100) / totalItems) // Update progress
                }
                json.append("],")

                // Encode countries
                json.append("\"countries\":[")
                appData.countries.forEachIndexed { index, countries ->
                    json.append(Json.encodeToString(countries))
                    if (index < appData.countries.size - 1) {
                        json.append(",")
                    }
                    processedItems++
                    onProgressUpdate((processedItems * 100) / totalItems) // Update progress
                }
                json.append("],")

                // Encode movies
                json.append("\"movies\":[")
                appData.movies.forEachIndexed { index, movie ->
                    json.append(Json.encodeToString(movie))
                    if (index < appData.movies.size - 1) {
                        json.append(",")
                    }
                    processedItems++
                    onProgressUpdate((processedItems * 100) / totalItems) // Update progress
                }
                json.append("],")

                // Encode series
                json.append("\"series\":[")
                appData.series.forEachIndexed { index, series ->
                    json.append(Json.encodeToString(series))
                    if (index < appData.series.size - 1) {
                        json.append(",")
                    }
                    processedItems++
                    onProgressUpdate((processedItems * 100) / totalItems) // Update progress
                }
                json.append("],")

                json.deleteCharAt(json.length - 1) // Remove trailing comma
                json.append("}")

                continuation.resume(json.toString())
            }

            jsonString
        }
    }
}