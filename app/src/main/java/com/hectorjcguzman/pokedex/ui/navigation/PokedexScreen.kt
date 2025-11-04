package com.hectorjcguzman.pokedex.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector

sealed class PokedexScreen(val route: String, val title: String, val icon: ImageVector) {
    object List : PokedexScreen("list", "Pokédex", Icons.Default.CatchingPokemon)
    object Encounter : PokedexScreen("encounter", "Encontrar", Icons.Default.TravelExplore)
    object Favorites : PokedexScreen("favorites", "Favoritos", Icons.Default.Favorite)
    object Detail : PokedexScreen("pokemon_detail", "Detalle", Icons.Default.CatchingPokemon)
}