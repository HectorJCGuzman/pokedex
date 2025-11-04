package com.hectorjcguzman.pokedex.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hectorjcguzman.pokedex.data.model.PokemonStatEntry
import com.hectorjcguzman.pokedex.data.model.PokemonTypeEntry

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromPokemonTypeEntryList(value: List<PokemonTypeEntry>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPokemonTypeEntryList(value: String): List<PokemonTypeEntry> {
        val listType = object : TypeToken<List<PokemonTypeEntry>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonStatEntryList(value: List<PokemonStatEntry>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPokemonStatEntryList(value: String): List<PokemonStatEntry> {
        val listType = object : TypeToken<List<PokemonStatEntry>>() {}.type
        return gson.fromJson(value, listType)
    }
}