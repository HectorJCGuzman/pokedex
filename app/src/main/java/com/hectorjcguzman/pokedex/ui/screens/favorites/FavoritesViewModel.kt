package com.hectorjcguzman.pokedex.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hectorjcguzman.pokedex.domain.usecase.GetFavoritePokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritePokemonUseCase: GetFavoritePokemonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesState())
    val uiState: StateFlow<FavoritesState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritePokemonUseCase().collect { favoritesList ->
                _uiState.update { it.copy(favorites = favoritesList) }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getFavoritePokemonUseCase: GetFavoritePokemonUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(getFavoritePokemonUseCase) as T
        }
    }
}