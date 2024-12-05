package com.kheemwel.mywatchlist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kheemwel.mywatchlist.data.local.database.dao.CountryDao
import com.kheemwel.mywatchlist.data.local.database.dao.GenreDao
import com.kheemwel.mywatchlist.data.local.database.dao.MovieDao
import com.kheemwel.mywatchlist.data.local.database.dao.SeriesDao
import com.kheemwel.mywatchlist.data.local.database.dao.StatusDao
import com.kheemwel.mywatchlist.data.local.database.entity.CountryEntity
import com.kheemwel.mywatchlist.data.local.database.entity.GenreEntity
import com.kheemwel.mywatchlist.data.local.database.entity.MovieEntity
import com.kheemwel.mywatchlist.data.local.database.entity.MovieGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesEntity
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.StatusEntity
import com.kheemwel.mywatchlist.data.util.DATABASE_VERSION

@Database(
    entities = [
        StatusEntity::class,
        GenreEntity::class,
        CountryEntity::class,
        MovieEntity::class,
        MovieGenresCrossRef::class,
        SeriesEntity::class,
        SeriesGenresCrossRef::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class MyWatchlistDatabase : RoomDatabase() {
    abstract fun statusDao(): StatusDao
    abstract fun genreDao(): GenreDao
    abstract fun countryDao(): CountryDao
    abstract fun movieDao(): MovieDao
    abstract fun seriesDao(): SeriesDao
}