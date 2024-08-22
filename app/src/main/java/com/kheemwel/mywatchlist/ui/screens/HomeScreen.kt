package com.kheemwel.mywatchlist.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.kheemwel.mywatchlist.core.LocalNavController
import com.kheemwel.mywatchlist.data.AppData
import com.kheemwel.mywatchlist.data.Movie
import com.kheemwel.mywatchlist.data.Series
import com.kheemwel.mywatchlist.ui.composables.MyWatchlistScaffold

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    MyWatchlistScaffold("Home", actions = {
        IconButton(onClick = { navController.navigate("/settings") }) {
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
        }
    }) { innerPadding ->
        var tabIndex by rememberSaveable { mutableIntStateOf(0) }

        val tabs = listOf("Movies", "Series")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> MovieTab()
                1 -> SeriesTab()
            }
        }
    }
}

@Composable
private fun MovieTab() {
    val horizontalScrollState = rememberScrollState()
    val navController = LocalNavController.current
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (column, fab) = createRefs()

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(state = horizontalScrollState)
            .constrainAs(column) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
            item {
                Row {
                    Cell {
                        Text("Title")
                    }
                    Cell {
                        Text("Duration")
                    }
                    Cell {
                        Text("Status")
                    }
                    Cell {
                        Text("Genre")
                    }
                    Cell {
                        Text("Country")
                    }
                    Cell {
                        Text("Favorite")
                    }
                }
            }
            items(AppData.movieList) { movie ->
                MovieRow(movie)
            }
        }

        FloatingActionButton(onClick = {
            navController.navigate("/add-movie")
        }, modifier = Modifier.constrainAs(fab) {
            absoluteRight.linkTo(parent.absoluteRight, margin = 16.dp)
            bottom.linkTo(parent.bottom, margin = 16.dp)
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieRow(movie: Movie) {
    var openDialog by remember { mutableStateOf(false) }

    Row(modifier = Modifier.combinedClickable(onClick = {}, onLongClick = { openDialog = true })) {
        Cell {
            Text(movie.title)
        }
        Cell {
            Text(movie.duration)
        }
        Cell {
            Text(movie.status)
        }
        Cell {
            Text(movie.genre)
        }
        Cell {
            Text(movie.country)
        }
        Cell {
            Text(movie.favorite.toString())
        }
    }

    if (openDialog) {
        Dialog(onDismissRequest = {
            openDialog = false
        }) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        movie.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text("Duration: ${movie.duration}")
                    Text("Status: ${movie.status}")
                    Text("Genre: ${movie.genre}")
                    Text("Country: ${movie.country}")
                    Text("Favorite: ${movie.favorite}")
                }
            }
        }
    }
}

@Composable
private fun Cell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp, 50.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun SeriesTab() {
    val horizontalScrollState = rememberScrollState()
    val navController = LocalNavController.current
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (column, fab) = createRefs()

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(state = horizontalScrollState)
            .constrainAs(column) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
            item {
                Row {
                    Cell {
                        Text("Title")
                    }
                    Cell {
                        Text("Seasons")
                    }
                    Cell {
                        Text("Episodes")
                    }
                    Cell {
                        Text("Status")
                    }
                    Cell {
                        Text("Genre")
                    }
                    Cell {
                        Text("Country")
                    }
                    Cell {
                        Text("Favorite")
                    }
                }
            }
            items(AppData.seriesList) {
                SeriesRow(it)
            }
        }

        FloatingActionButton(onClick = {
            navController.navigate("/add-series")
        }, modifier = Modifier.constrainAs(fab) {
            absoluteRight.linkTo(parent.absoluteRight, margin = 16.dp)
            bottom.linkTo(parent.bottom, margin = 16.dp)
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SeriesRow(series: Series) {
    var openDialog by remember { mutableStateOf(false) }

    Row(modifier = Modifier.combinedClickable(onClick = {}, onLongClick = { openDialog = true })) {
        Cell {
            Text(series.title)
        }
        Cell {
            Text(series.seasons.toString())
        }
        Cell {
            Text(series.episodes.toString())
        }
        Cell {
            Text(series.status)
        }
        Cell {
            Text(series.genre)
        }
        Cell {
            Text(series.country)
        }
        Cell {
            Text(series.favorite.toString())
        }
    }

    if (openDialog) {
        Dialog(onDismissRequest = {
            openDialog = false
        }) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        series.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text("Seasons: ${series.seasons}")
                    Text("Episodes: ${series.episodes}")
                    Text("Genre: ${series.genre}")
                    Text("Country: ${series.country}")
                    Text("Favorite: ${series.favorite}")
                }
            }
        }
    }
}