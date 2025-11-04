package com.hectorjcguzman.pokedex.ui.screens.list

import com.hectorjcguzman.pokedex.data.local.PokemonEntity

data class PokedexListState(
    val pokemonList: List<PokemonEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isOnline: Boolean = true
)