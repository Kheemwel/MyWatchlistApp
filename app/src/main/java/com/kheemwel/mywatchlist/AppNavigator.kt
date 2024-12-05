package com.kheemwel.mywatchlist

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kheemwel.mywatchlist.presentation.screens.countries_creen.CountriesScreen
import com.kheemwel.mywatchlist.presentation.screens.genres_screen.GenresScreen
import com.kheemwel.mywatchlist.presentation.screens.home_screen.HomeScreen
import com.kheemwel.mywatchlist.presentation.screens.settings_screen.SettingsScreen
import com.kheemwel.mywatchlist.presentation.screens.statuses_screen.StatusesScreen
import com.kheemwel.mywatchlist.presentation.theme.AppTheme
import com.kheemwel.mywatchlist.presentation.util.Routes

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen.path,
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
            composable(Routes.HomeScreen.path) { HomeScreen(navController) }
            composable(Routes.SettingsScreen.path) { SettingsScreen(navController) }
            composable(Routes.StatusesScreen.path) { StatusesScreen(navController) }
            composable(Routes.GenresScreen.path) { GenresScreen(navController) }
            composable(Routes.CountriesScreen.path) { CountriesScreen(navController) }
        }
    }
}