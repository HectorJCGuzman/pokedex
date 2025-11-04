package com.hectorjcguzman.pokedex.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_details ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_details WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_details WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Query("SELECT COUNT(id) FROM pokemon_details WHERE wasEncountered = 0")
    suspend fun getSequentialPokemonCount(): Int

    @Query("UPDATE pokemon_details SET isFavorite = :isFavorite WHERE id = :pokemonId")
    suspend fun updateFavoriteStatus(pokemonId: Int, isFavorite: Boolean)
}