package com.kheemwel.mywatchlist.utils

import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
import java.util.UUID
import kotlin.random.Random

fun generateDummyMovies(
    count: Int,
    movieModel: MovieModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>
) {

    for (c in 1..count) {
        movieModel.addMovie(
            Movie(
                uuid = UUID.randomUUID().toString(),
                title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
                genres = genres.shuffled().take(Random.nextInt(1, genres.size)),
                country = countries.random(),
                status = statuses.random(),
                isFavorite = Random.nextBoolean(),
                releaseDate = generateRandomDate(),
                lastModified = generateRandomDate(),
            )
        )
    }
}

fun generateStatuses(statusModel: StatusModel) {
    val statuses = listOf(
        "Pending",
        "Still Watching",
        "Finished",
        "Waiting"
    )
    statuses.forEach {
        statusModel.addStatus(it)
    }
}

fun generateGenres(genreModel: GenreModel) {
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
    genres.forEach {
        genreModel.addGenre(it)
    }
}

fun generateCountries(countryModel: CountryModel) {
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
    countries.forEach {
        countryModel.addCountry(it)
    }
}

fun generateDummySeries(
    count: Int,
    seriesModel: SeriesModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>
) {

    for (c in 1..count) {
        seriesModel.addSeries(
            Series(
                uuid = UUID.randomUUID().toString(),
                title = generateRandomSentence(Random.nextInt(1, 4), Random.nextInt(3, 10)),
                season = Random.nextInt(1, 16),
                episode = Random.nextInt(1, 32),
                genres = genres.shuffled().take(Random.nextInt(1, genres.size)),
                country = countries.random(),
                status = statuses.random(),
                isFavorite = Random.nextBoolean(),
                releaseDate = generateRandomDate(),
                lastModified = generateRandomDate(),
            )
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