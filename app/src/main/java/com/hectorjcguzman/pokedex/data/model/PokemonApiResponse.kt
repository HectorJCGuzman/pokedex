package com.hectorjcguzman.pokedex.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)

data class PokemonDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeEntry>,
    val sprites: PokemonSprites,
    val stats: List<PokemonStatEntry>
)

data class PokemonTypeEntry(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)

data class PokemonSprites(
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String
)

data class PokemonStatEntry(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: PokemonStat
)

data class PokemonStat(
    val name: String
)