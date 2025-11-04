package com.hectorjcguzman.pokedex.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hectorjcguzman.pokedex.data.PokemonRepository
import com.hectorjcguzman.pokedex.domain.usecase.ToggleFavoritePokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokemonId: Int,
    private val repository: PokemonRepository,
    private val toggleFavoriteUseCase: ToggleFavoritePokemonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val uiState: StateFlow<PokemonDetailState> = _uiState.asStateFlow()

    init {
        loadPokemonDetails()
    }

    private fun loadPokemonDetails() {
        viewModelScope.launch {
            repository.getPokemonById(pokemonId).collectLatest { pokemon ->
                if (pokemon != null) {
                    _uiState.value = PokemonDetailState.Success(pokemon)
                } else {
                    _uiState.value = PokemonDetailState.Error("Pokémon not found.")
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(pokemonId)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val pokemonId: Int,
        private val repository: PokemonRepository,
        private val toggleFavoriteUseCase: ToggleFavoritePokemonUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokemonDetailViewModel(pokemonId, repository, toggleFavoriteUseCase) as T
        }
    }
}