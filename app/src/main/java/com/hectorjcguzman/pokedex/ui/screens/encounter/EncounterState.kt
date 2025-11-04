package com.hectorjcguzman.pokedex.ui.screens.encounter

import com.hectorjcguzman.pokedex.data.EncounterResult

data class EncounterState(
    val encounterStatus: EncounterStatus = EncounterStatus.Idle,
    val isOnline: Boolean = true
)

sealed class EncounterStatus {
    object Idle : EncounterStatus()
    object Searching : EncounterStatus()
    data class Found(val result: EncounterResult) : EncounterStatus()
    data class Error(val message: String) : EncounterStatus()
}