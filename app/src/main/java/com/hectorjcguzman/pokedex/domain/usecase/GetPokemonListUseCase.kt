package com.hectorjcguzman.pokedex.domain.usecase

import com.hectorjcguzman.pokedex.data.PokemonRepository
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import kotlinx.coroutines.flow.Flow

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    fun getPokemonList(): Flow<List<PokemonEntity>> = repository.allPokemon

    suspend fun fetchMorePokemon(offset: Int, limit: Int) {
        repository.fetchAndSavePokemon(offset, limit)
    }
}