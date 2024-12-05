package com.kheemwel.mywatchlist.presentation.screens.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.presentation.composables.WatchTile


@Composable
fun HomePrimaryTab(
    title: String,
    badge: String,
    icon: @Composable (() -> Unit)? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title)
                Text(
                    text = badge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .padding(horizontal = 6.dp)
                )
            }
        },
        icon = icon,
        selected = selected,
        onClick = onClick,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun MovieTab(
    movies: List<Movie>,
    selectionMode: Boolean = false,
    enableSelectionMode: () -> Unit = {},
    selectedItems: List<Long> = emptyList(),
    onItemSelected: (Long) -> Unit = {},
    onToggleFavorite: (Movie) -> Unit = {},
    onView: (Movie) -> Unit,
    onEdit: (Movie) -> Unit,
    onDelete: (Movie) -> Unit
) {
    LazyColumn {
        items(movies) { movie ->
            WatchTile(
                text = movie.title,
                status = movie.status?.name ?: "",
                isFavorite = movie.isFavorite,
                onFavorite = { onToggleFavorite(movie) },
                onEdit = { onEdit(movie) },
                onDelete = { onDelete(movie) },
                selectionMode = selectionMode,
                selected = selectedItems.contains(movie.id),
                onClick = {
                    if (selectionMode) {
                        onItemSelected(movie.id)
                    } else {
                        onView(movie)
                    }
                },
                onLongClick = {
                    enableSelectionMode()
                    onItemSelected(movie.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
fun SeriesTab(
    series: List<Series>,
    selectionMode: Boolean = false,
    enableSelectionMode: () -> Unit = {},
    selectedItems: List<Long> = emptyList(),
    onItemSelected: (Long) -> Unit = {},
    onToggleFavorite: (Series) -> Unit = {},
    onView: (Series) -> Unit,
    onEdit: (Series) -> Unit,
    onDelete: (Series) -> Unit
) {
    LazyColumn {
        items(series) { series ->
            WatchTile(
                text = series.title,
                status = series.status?.name ?: "",
                isFavorite = series.isFavorite,
                onFavorite = { onToggleFavorite(series) },
                onEdit = { onEdit(series) },
                onDelete = { onDelete(series) },
                selectionMode = selectionMode,
                selected = selectedItems.contains(series.id),
                onClick = {
                    if (selectionMode) {
                        onItemSelected(series.id)
                    } else {
                        onView(series)
                    }
                },
                onLongClick = {
                    enableSelectionMode()
                    onItemSelected(series.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
