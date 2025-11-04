package com.hectorjcguzman.pokedex.ui.screens.favorites

import com.hectorjcguzman.pokedex.data.local.PokemonEntity

data class FavoritesState(
    val favorites: List<PokemonEntity> = emptyList()
)