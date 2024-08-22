package com.kheemwel.mywatchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kheemwel.mywatchlist.core.LocalNavController
import com.kheemwel.mywatchlist.ui.screens.AddMovieScreen
import com.kheemwel.mywatchlist.ui.screens.AddSeriesScreen
import com.kheemwel.mywatchlist.ui.screens.HomeScreen
import com.kheemwel.mywatchlist.ui.screens.SettingsScreen
import com.kheemwel.mywatchlist.ui.theme.MyWatchlistAppTheme

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
    CompositionLocalProvider(LocalNavController provides navController) {
        MyWatchlistAppTheme {
            NavHost(
                navController = navController,
                startDestination = "/home",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable("/home") { HomeScreen() }
                composable("/settings") { SettingsScreen() }
                composable("/add-movie") { AddMovieScreen() }
                composable("/add-series") { AddSeriesScreen() }
            }
        }
    }
}

