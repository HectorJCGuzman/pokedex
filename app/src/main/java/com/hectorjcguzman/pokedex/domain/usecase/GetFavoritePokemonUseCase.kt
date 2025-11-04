package com.hectorjcguzman.pokedex.domain.usecase

import com.hectorjcguzman.pokedex.data.PokemonRepository
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import kotlinx.coroutines.flow.Flow

class GetFavoritePokemonUseCase(private val repository: PokemonRepository) {
    operator fun invoke(): Flow<List<PokemonEntity>> {
        return repository.favoritePokemon
    }
}