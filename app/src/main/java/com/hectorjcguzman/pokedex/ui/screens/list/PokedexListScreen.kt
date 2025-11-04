package com.hectorjcguzman.pokedex.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hectorjcguzman.pokedex.ui.common.PokemonCard
import com.hectorjcguzman.pokedex.ui.common.SkeletonCard

@Composable
fun PokedexListScreen(
    viewModel: PokedexListViewModel,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            if (gridState.layoutInfo.visibleItemsInfo.isEmpty()) {
                false
            } else {
                val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.last()
                lastVisibleItem.index >= gridState.layoutInfo.totalItemsCount - 5
            }
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !uiState.isLoading) {
            viewModel.loadMorePokemon()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.pokemonList, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) }
            )
        }

        if (uiState.isLoading) {
            items(4) {
                SkeletonCard()
            }
        }
    }
}