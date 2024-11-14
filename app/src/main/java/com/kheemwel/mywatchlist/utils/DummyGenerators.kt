package com.kheemwel.mywatchlist.utils

import com.kheemwel.mywatchlist.data.models.AppData
import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.random.Random

val statuses = listOf(
    "Pending",
    "Still Watching",
    "Finished",
    "Waiting"
)

val genres = listOf(
    "SciFi",
    "Thriller",
    "Fantasy",
    "Romance",
    "Drama",
    "Action",
    "Adventure",
    "Comedy",
    "Horror",
    "Animation",
    "Anime",
    "Cartoon",
    "Documentary",
    "Family",
    "History",
    "Musical",
    "Mystery",
    "War",
    "Western",
    "Asian",
    "BL",
    "GL",
    "LGBTQIA+",
    "Hollywood",
    "Bollywood",
    "Netflix"
)


val countries = listOf(
    "America",
    "UK",
    "Korea",
    "Japan",
    "China",
    "Taiwan",
    "Thailand",
    "Philippines",
    "Indonesia",
    "India"
)

fun generateStatuses(statusModel: StatusModel) {
    statuses.forEach {
        statusModel.addStatus(it)
    }
}

fun generateGenres(genreModel: GenreModel) {

    genres.forEach {
        genreModel.addGenre(it)
    }
}

fun generateCountries(countryModel: CountryModel) {
    countries.forEach {
        countryModel.addCountry(it)
    }
}

fun <T> generateList(count: Int, itemGenerator: (Int) -> T): List<T> {
    return List(count) { index -> itemGenerator(index) }
}

fun generateMovieList(
    count: Int,
    genreList: List<String> = genres,
    countryList: List<String> = countries,
    statusList: List<String> = statuses
): List<Movie> {
    return generateList(count) {
        Movie(
            uuid = UUID.randomUUID().toString(),
            title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
            genres = genreList.shuffled().take(Random.nextInt(1, genres.size)),
            country = countryList.random(),
            status = statusList.random(),
            isFavorite = Random.nextBoolean(),
            releaseDate = generateRandomDate(),
            lastModified = generateRandomDate(),
        )
    }
}

fun generateDummyMovies(
    count: Int,
    movieModel: MovieModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>
) {
    val movies = generateMovieList(count, genres, countries, statuses)
    movies.forEach {
        movieModel.addMovie(it)
    }
}

fun generateSeriesList(
    count: Int,
    genreList: List<String> = genres,
    countryList: List<String> = countries,
    statusList: List<String> = statuses
): List<Series> {
    return generateList(count) {
        Series(
            uuid = UUID.randomUUID().toString(),
            title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
            season = Random.nextInt(1, 16),
            episode = Random.nextInt(1, 32),
            genres = genreList.shuffled().take(Random.nextInt(1, genres.size)),
            country = countryList.random(),
            status = statusList.random(),
            isFavorite = Random.nextBoolean(),
            releaseDate = generateRandomDate(),
            lastModified = generateRandomDate(),
        )
    }
}

fun generateDummySeries(
    count: Int,
    seriesModel: SeriesModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>
) {
    val series = generateSeriesList(count, genres, countries, statuses)
    series.forEach {
        seriesModel.addSeries(it)
    }
}

fun generateAppDataJson(countMovies: Int = 100, countSeries: Int = 100): String {
    val movies = generateMovieList(countMovies)
    val series = generateSeriesList(countSeries)
    val appData = AppData(statuses, genres, countries, movies, series)
    return Json.encodeToString(appData)
}

fun generateRandomDate(): String {
    val year = Random.nextInt(2000, 2024).toString()
    val month = Random.nextInt(1, 13).toString().padStart(2, '0')
    val day = Random.nextInt(1, 29).toString().padStart(2, '0')
    return "$year-$month-$day"
}

fun generateRandomAlphanumericString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun generateRandomSentence(wordCount: Int, lettersPerWord: Int): String {
    val sentence = (1..wordCount).joinToString(" ") {
        generateRandomAlphanumericString(lettersPerWord)
    }
    return sentence
}