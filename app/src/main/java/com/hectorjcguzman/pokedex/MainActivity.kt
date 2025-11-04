package com.hectorjcguzman.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.hectorjcguzman.pokedex.ui.navigation.PokedexAppNavigation
import com.hectorjcguzman.pokedex.ui.theme.PokedexTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = application as PokedexApplication
        setContent {
            PokedexTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                PokedexAppNavigation(
                    windowSizeClass = windowSizeClass,
                    application = application
                )
            }
        }
    }
}