package com.hectorjcguzman.pokedex

import android.app.Application
import com.hectorjcguzman.pokedex.di.AppContainer

class PokedexApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}