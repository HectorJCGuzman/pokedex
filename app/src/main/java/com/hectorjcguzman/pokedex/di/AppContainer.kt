package com.hectorjcguzman.pokedex.di

import android.content.Context
import com.hectorjcguzman.pokedex.data.PokemonRepository
import com.hectorjcguzman.pokedex.data.PokemonRepositoryImpl
import com.hectorjcguzman.pokedex.data.local.AppDatabase
import com.hectorjcguzman.pokedex.data.location.LocationService
import com.hectorjcguzman.pokedex.data.network.ConnectivityObserver
import com.hectorjcguzman.pokedex.data.network.RetrofitInstance
import com.hectorjcguzman.pokedex.domain.usecase.FindRandomPokemonUseCase
import com.hectorjcguzman.pokedex.domain.usecase.GetFavoritePokemonUseCase
import com.hectorjcguzman.pokedex.domain.usecase.GetPokemonListUseCase
import com.hectorjcguzman.pokedex.domain.usecase.ToggleFavoritePokemonUseCase

class AppContainer(private val context: Context) {

    private val pokemonDao by lazy { AppDatabase.getDatabase(context).pokemonDao() }
    private val pokeApiService by lazy { RetrofitInstance.api }

    val connectivityObserver by lazy { ConnectivityObserver(context) }
    val locationService by lazy { LocationService(context) }

    val pokemonRepository: PokemonRepository by lazy {
        PokemonRepositoryImpl(pokeApiService, pokemonDao, context)
    }

    val getPokemonListUseCase by lazy { GetPokemonListUseCase(pokemonRepository) }
    val getFavoritePokemonUseCase by lazy { GetFavoritePokemonUseCase(pokemonRepository) }
    val findRandomPokemonUseCase by lazy { FindRandomPokemonUseCase(pokemonRepository) }
    val toggleFavoritePokemonUseCase by lazy { ToggleFavoritePokemonUseCase(pokemonRepository) }
}