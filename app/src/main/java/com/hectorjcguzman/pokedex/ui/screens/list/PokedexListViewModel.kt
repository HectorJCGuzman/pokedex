package com.hectorjcguzman.pokedex.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hectorjcguzman.pokedex.data.network.ConnectivityObserver
import com.hectorjcguzman.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokedexListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokedexListState())
    val uiState: StateFlow<PokedexListState> = _uiState.asStateFlow()

    private val _offset = MutableStateFlow(0)
    private val limit = 25

    init {
        observePokemonList()
        observeConnectivity()
        loadMorePokemon()
    }

    private fun observePokemonList() {
        viewModelScope.launch {
            getPokemonListUseCase.getPokemonList().collect { pokemon ->
                _uiState.update { it.copy(pokemonList = pokemon) }
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { isOnline ->
                _uiState.update { it.copy(isOnline = isOnline) }
            }
        }
    }

    fun loadMorePokemon() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPokemonListUseCase.fetchMorePokemon(offset = _offset.value, limit = limit)
            _offset.value += limit
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getPokemonListUseCase: GetPokemonListUseCase,
        private val connectivityObserver: ConnectivityObserver
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokedexListViewModel(getPokemonListUseCase, connectivityObserver) as T
        }
    }
}