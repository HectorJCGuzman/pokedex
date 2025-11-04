package com.hectorjcguzman.pokedex.domain.usecase

import com.hectorjcguzman.pokedex.data.EncounterResult
import com.hectorjcguzman.pokedex.data.PokemonRepository

class FindRandomPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(): Result<EncounterResult> {
        return repository.findRandomPokemon()
    }
}