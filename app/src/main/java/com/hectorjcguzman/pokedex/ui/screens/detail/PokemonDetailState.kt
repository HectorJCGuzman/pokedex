package com.hectorjcguzman.pokedex.ui.screens.detail

import com.hectorjcguzman.pokedex.data.local.PokemonEntity

sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Success(val pokemon: PokemonEntity) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}