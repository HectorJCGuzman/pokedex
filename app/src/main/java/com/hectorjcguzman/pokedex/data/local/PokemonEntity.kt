package com.hectorjcguzman.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hectorjcguzman.pokedex.data.model.PokemonStatEntry
import com.hectorjcguzman.pokedex.data.model.PokemonTypeEntry

@Entity(tableName = "pokemon_details")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeEntry>,
    val imageUrl: String,
    val stats: List<PokemonStatEntry>,
    var isFavorite: Boolean = false,
    var localImagePath: String? = null,
    var wasEncountered: Boolean = false
)