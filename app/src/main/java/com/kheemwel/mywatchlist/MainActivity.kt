package com.kheemwel.mywatchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kheemwel.mywatchlist.ui.screens.HomeScreen
import com.kheemwel.mywatchlist.ui.screens.SettingsScreen
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
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = "/home",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable("/home") { HomeScreen(navController) }
            composable("/settings") { SettingsScreen(navController) }
        }
    }
}