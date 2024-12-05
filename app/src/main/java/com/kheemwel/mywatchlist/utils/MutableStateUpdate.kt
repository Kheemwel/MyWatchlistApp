package com.kheemwel.mywatchlist.utils

import androidx.compose.runtime.MutableState

/**
 * Extension function to update the value of a [MutableState] in a concise way.
 *
 * This function applies a transformation to the current state value and updates it.
 * It's useful for modifying state without reassigning the whole object.
 *
 * Example:
 * ```kotlin
 * val state = mutableStateOf(ScreenState())
 * state.update { copy(isLoading = true) }
 * ```
 *
 * @param update A function that transforms the current state value.
 */
inline fun <T> MutableState<T>.update(update: T.() -> T) {
    value = value.update()
}
