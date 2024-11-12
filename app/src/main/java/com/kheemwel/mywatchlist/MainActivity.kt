package com.kheemwel.mywatchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kheemwel.mywatchlist.data.database.SharedPref
import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
import com.kheemwel.mywatchlist.ui.screens.CountriesScreen
import com.kheemwel.mywatchlist.ui.screens.GenresScreen
import com.kheemwel.mywatchlist.ui.screens.SettingsScreen
import com.kheemwel.mywatchlist.ui.screens.StatusesScreen
import com.kheemwel.mywatchlist.ui.screens.home.HomeScreen
import com.kheemwel.mywatchlist.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    Main()
}

@Composable
private fun Main() {
    val navController = rememberNavController()

    SharedPref.init(LocalContext.current)

    val movieModel = MovieModel()
    val seriesModel = SeriesModel()
    val statusModel = StatusModel()
    val genreModel = GenreModel()
    val countryModel = CountryModel()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = "/home",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(700)
                )
            },
            popEnterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(700)
                )
            }
        ) {
            composable("/home") {
                HomeScreen(
                    navController,
                    movieModel,
                    seriesModel,
                    statusModel,
                    genreModel,
                    countryModel
                )
            }
            composable("/settings") { SettingsScreen(navController) }
            composable("/settings/statuses") { StatusesScreen(navController, statusModel) }
            composable("/settings/genres") { GenresScreen(navController, genreModel) }
            composable("/settings/countries") { CountriesScreen(navController, countryModel) }
        }
    }
}