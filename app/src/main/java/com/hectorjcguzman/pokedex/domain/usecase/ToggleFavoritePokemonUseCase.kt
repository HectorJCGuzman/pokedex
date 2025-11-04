package com.hectorjcguzman.pokedex.domain.usecase

import com.hectorjcguzman.pokedex.data.PokemonRepository

class ToggleFavoritePokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(pokemonId: Int) {
        repository.toggleFavorite(pokemonId)
    }
}