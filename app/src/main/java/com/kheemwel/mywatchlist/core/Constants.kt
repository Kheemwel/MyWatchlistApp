package com.kheemwel.mywatchlist.core

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

/**
 * Provides NavController to all screens
 */
val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}