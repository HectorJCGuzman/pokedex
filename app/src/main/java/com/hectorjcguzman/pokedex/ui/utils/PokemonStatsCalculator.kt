package com.hectorjcguzman.pokedex.ui.utils

import kotlin.math.floor

object PokemonStatsCalculator {
    private const val MAX_IV = 31
    private const val MAX_EV = 63
    private const val BENEFICIAL_NATURE = 1.1

    fun calculateMaxStat(baseStat: Int, statName: String): Int {
        if (statName.equals("hp", ignoreCase = true)) {
            return 2 * baseStat + MAX_IV + (MAX_EV / 4) + 110
        }
        val value = (2 * baseStat + MAX_IV + (MAX_EV / 4) + 5) * BENEFICIAL_NATURE
        return floor(value).toInt()
    }
}