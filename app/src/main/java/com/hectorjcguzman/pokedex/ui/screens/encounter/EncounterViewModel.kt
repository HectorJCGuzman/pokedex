package com.hectorjcguzman.pokedex.ui.screens.encounter

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hectorjcguzman.pokedex.data.location.LocationService
import com.hectorjcguzman.pokedex.data.network.ConnectivityObserver
import com.hectorjcguzman.pokedex.domain.usecase.FindRandomPokemonUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EncounterViewModel(
    private val findRandomPokemonUseCase: FindRandomPokemonUseCase,
    private val locationService: LocationService,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(EncounterState())
    val uiState: StateFlow<EncounterState> = _uiState.asStateFlow()

    private var locationJob: Job? = null
    private var lastKnownLocation: Location? = null
    private val encounterDistanceThreshold = 10.0f

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { isOnline ->
                _uiState.update { it.copy(isOnline = isOnline) }
            }
        }
    }

    fun startLocationUpdates() {
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
            locationService.requestLocationUpdates().collect { newLocation ->
                val lastLocation = lastKnownLocation
                if (lastLocation != null) {
                    val distance = lastLocation.distanceTo(newLocation)
                    if (distance >= encounterDistanceThreshold) {
                        lastKnownLocation = newLocation
                        searchForRandomPokemon(isTriggeredByWalk = true)
                    }
                } else {
                    lastKnownLocation = newLocation
                }
            }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
        lastKnownLocation = null
    }

    fun searchForRandomPokemon(isTriggeredByWalk: Boolean = false) {
        if (_uiState.value.encounterStatus is EncounterStatus.Searching) return

        viewModelScope.launch {
            if (_uiState.value.isOnline) {
                if (isTriggeredByWalk) {
                    _uiState.update { it.copy(encounterStatus = EncounterStatus.Searching) }
                }
                findRandomPokemonUseCase()
                    .onSuccess { result ->
                        _uiState.update { it.copy(encounterStatus = EncounterStatus.Found(result)) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(encounterStatus = EncounterStatus.Error("No se pudo encontrar un Pokémon.")) }
                    }
            } else {
                _uiState.update { it.copy(encounterStatus = EncounterStatus.Error("Se necesita conexión a internet.")) }
            }
        }
    }

    fun resetEncounterState() {
        _uiState.update { it.copy(encounterStatus = EncounterStatus.Idle) }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val findRandomPokemonUseCase: FindRandomPokemonUseCase,
        private val locationService: LocationService,
        private val connectivityObserver: ConnectivityObserver
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EncounterViewModel(findRandomPokemonUseCase, locationService, connectivityObserver) as T
        }
    }
}