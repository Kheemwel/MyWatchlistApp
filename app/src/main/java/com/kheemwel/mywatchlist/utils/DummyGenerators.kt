package com.kheemwel.mywatchlist.utils

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.model.Status
import kotlin.random.Random

val dummyStatuses = listOf(
    "Pending",
    "Still Watching",
    "Finished",
    "Waiting"
)

val dummyGenres = listOf(
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


val dummyCountries = listOf(
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

fun generateStatuses(): List<Status> {
    return dummyStatuses.map { Status(name = it) }
}

fun generateGenres(): List<Genre> {
    return dummyGenres.map { Genre(name = it) }
}

fun generateCountries(): List<Country> {
    return dummyCountries.map { Country(name = it) }
}

fun <T> generateList(count: Int, itemGenerator: (Int) -> T): List<T> {
    return List(count) { index -> itemGenerator(index) }
}

fun generateMovieList(
    count: Int,
    statusList: List<Status>,
    countryList: List<Country>,
    genreList: List<Genre>
): List<Movie> {
    return generateList(count) {
        Movie(
            title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
            country = countryList.random(),
            status = statusList.random(),
            genres = genreList.shuffled().take(Random.nextInt(1, genreList.size)),
            isFavorite = Random.nextBoolean(),
            releaseDate = generateRandomDate(),
            lastModified = generateRandomDate(),
        )
    }
}

fun generateSeriesList(
    count: Int,
    statusList: List<Status>,
    countryList: List<Country>,
    genreList: List<Genre>
): List<Series> {
    return generateList(count) {
        Series(
            title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
            season = Random.nextInt(1, 16),
            episode = Random.nextInt(1, 32),
            genres = genreList.shuffled().take(Random.nextInt(1, genreList.size)),
            country = countryList.random(),
            status = statusList.random(),
            isFavorite = Random.nextBoolean(),
            releaseDate = generateRandomDate(),
            lastModified = generateRandomDate(),
        )
    }
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