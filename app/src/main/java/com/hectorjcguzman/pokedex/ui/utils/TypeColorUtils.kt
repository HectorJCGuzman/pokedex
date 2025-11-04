package com.hectorjcguzman.pokedex.ui.utils

import androidx.compose.ui.graphics.Color

fun getColorForType(type: String): Color {
    return when (type) {
        "grass" -> Color(0xFF4CAF50)
        "fire" -> Color(0xFFF44336)
        "water" -> Color(0xFF2196F3)
        "electric" -> Color(0xFFF9D006)
        "fairy" -> Color(0xFFE91E63)
        "normal" -> Color(0xFFA1887F)
        "poison" -> Color(0xFF673AB7)
        "ghost" -> Color(0xFF757575)
        "flying" -> Color(0xFF81D4FA)
        "bug" -> Color(0xFF8BC34A)
        "psychic" -> Color(0xFFEC407A)
        "rock" -> Color(0xFF795548)
        "ground" -> Color(0xFFBCAAA4)
        "fighting" -> Color(0xFFE57373)
        "steel" -> Color(0xFF90A4AE)
        "ice" -> Color(0xFFB3E5FC)
        "dragon" -> Color(0xFF7E57C2)
        "dark" -> Color(0xFF424242)
        else -> Color.Gray
    }
}

fun getTextColorForType(backgroundColor: Color): Color {
    val luminance = (0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue)
    return if (luminance < 0.5) Color.White else Color.Black
}