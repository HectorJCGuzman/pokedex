package com.hectorjcguzman.pokedex.data

import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import kotlinx.coroutines.flow.Flow

data class EncounterResult(
    val pokemon: PokemonEntity,
    val isNew: Boolean
)

interface PokemonRepository {
    val allPokemon: Flow<List<PokemonEntity>>
    val favoritePokemon: Flow<List<PokemonEntity>>
    suspend fun fetchAndSavePokemon(offset: Int, limit: Int)
    suspend fun findRandomPokemon(): Result<EncounterResult>
    suspend fun toggleFavorite(pokemonId: Int)
    fun getPokemonById(pokemonId: Int): Flow<PokemonEntity?>
}