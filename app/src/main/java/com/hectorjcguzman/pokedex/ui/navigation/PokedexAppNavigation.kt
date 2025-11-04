package com.hectorjcguzman.pokedex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hectorjcguzman.pokedex.PokedexApplication
import com.hectorjcguzman.pokedex.ui.common.PokedexTopAppBar
import com.hectorjcguzman.pokedex.ui.screens.detail.PokemonDetailScreen
import com.hectorjcguzman.pokedex.ui.screens.detail.PokemonDetailViewModel
import com.hectorjcguzman.pokedex.ui.screens.encounter.EncounterScreen
import com.hectorjcguzman.pokedex.ui.screens.encounter.EncounterViewModel
import com.hectorjcguzman.pokedex.ui.screens.favorites.FavoritesScreen
import com.hectorjcguzman.pokedex.ui.screens.favorites.FavoritesViewModel
import com.hectorjcguzman.pokedex.ui.screens.list.PokedexListScreen
import com.hectorjcguzman.pokedex.ui.screens.list.PokedexListViewModel

@Composable
fun PokedexAppNavigation(
    windowSizeClass: WindowSizeClass,
    application: PokedexApplication
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val isOnline by application.container.connectivityObserver.observe().collectAsState(initial = true)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute?.startsWith(PokedexScreen.Detail.route) == false) {
                PokedexTopAppBar(isOnline = isOnline)
            }
        },
        bottomBar = {
            val isDetailScreen = currentRoute?.startsWith(PokedexScreen.Detail.route) == true
            if (!isDetailScreen) {
                PokedexBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PokedexScreen.List.route,
        ) {
            composable(route = PokedexScreen.List.route) {
                val viewModel: PokedexListViewModel = viewModel(
                    factory = PokedexListViewModel.Factory(
                        application.container.getPokemonListUseCase,
                        application.container.connectivityObserver
                    )
                )
                PokedexListScreen(
                    viewModel = viewModel,
                    onPokemonClick = { pokemonId ->
                        navController.navigate("${PokedexScreen.Detail.route}/$pokemonId")
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(route = PokedexScreen.Encounter.route) {
                val viewModel: EncounterViewModel = viewModel(
                    factory = EncounterViewModel.Factory(
                        application.container.findRandomPokemonUseCase,
                        application.container.locationService,
                        application.container.connectivityObserver
                    )
                )
                EncounterScreen(
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    onPokemonClick = { pokemonId ->
                        navController.navigate("${PokedexScreen.Detail.route}/$pokemonId")
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(route = PokedexScreen.Favorites.route) {
                val viewModel: FavoritesViewModel = viewModel(
                    factory = FavoritesViewModel.Factory(application.container.getFavoritePokemonUseCase)
                )
                FavoritesScreen(
                    viewModel = viewModel,
                    onPokemonClick = { pokemonId ->
                        navController.navigate("${PokedexScreen.Detail.route}/$pokemonId")
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(
                route = "${PokedexScreen.Detail.route}/{pokemonId}",
                arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
            ) { backStackEntry ->
                val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: -1
                val viewModel: PokemonDetailViewModel = viewModel(
                    factory = PokemonDetailViewModel.Factory(
                        pokemonId,
                        application.container.pokemonRepository,
                        application.container.toggleFavoritePokemonUseCase
                    )
                )
                PokemonDetailScreen(
                    viewModel = viewModel,
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                )
            }
        }
    }
}


@Composable
fun PokedexBottomBar(navController: NavHostController) {
    val items = listOf(
        PokedexScreen.List,
        PokedexScreen.Encounter,
        PokedexScreen.Favorites
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            //restoreState = true
                        }
                    }
                }
            )
        }
    }
}