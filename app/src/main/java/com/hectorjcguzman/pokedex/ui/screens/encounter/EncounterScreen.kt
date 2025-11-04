package com.hectorjcguzman.pokedex.ui.screens.encounter

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import com.hectorjcguzman.pokedex.ui.utils.getColorForType
import com.hectorjcguzman.pokedex.ui.utils.getTextColorForType
import java.util.*

@Composable
fun EncounterScreen(
    viewModel: EncounterViewModel,
    snackbarHostState: SnackbarHostState,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var hasLocationPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                hasLocationPermission = true
                viewModel.startLocationUpdates()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLocationUpdates()
        }
    }

    LaunchedEffect(uiState.encounterStatus) {
        if (uiState.encounterStatus is EncounterStatus.Found) {
            val result = (uiState.encounterStatus as EncounterStatus.Found).result
            val message = if (result.isNew) "¡Nuevo Pokémon encontrado!" else "Pokémon encontrado (ya registrado)"
            snackbarHostState.showSnackbar(message)
            vibrate(context)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!uiState.isOnline) {
                OfflineMessage()
            } else if (!hasLocationPermission) {
                PermissionMessage(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) })
            } else {
                AnimatedContent(
                    targetState = uiState.encounterStatus,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = fadeIn(animationSpec = tween(300)),
                            initialContentExit = fadeOut(animationSpec = tween(300))
                        )
                    }
                ) { state ->
                    when (state) {
                        is EncounterStatus.Idle -> IdleView(viewModel)
                        is EncounterStatus.Searching -> SearchingView()
                        is EncounterStatus.Found -> {
                            val shakeController = remember { Animatable(0f) }
                            LaunchedEffect(state) {
                                shakeController.animateTo(
                                    targetValue = 0f,
                                    animationSpec = keyframes {
                                        durationMillis = 600
                                        -10f at 50; 10f at 100
                                        -10f at 150; 10f at 200
                                        -5f at 250; 5f at 300
                                        -2f at 350; 2f at 400
                                        0f at 450
                                    }
                                )
                            }
                            FoundView(
                                pokemon = state.result.pokemon,
                                modifier = Modifier.offset(x = shakeController.value.dp),
                                onViewDetailsClick = { onPokemonClick(state.result.pokemon.id) },
                                onContinueSearchingClick = { viewModel.resetEncounterState() }
                            )
                        }
                        is EncounterStatus.Error -> ErrorView(state.message, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun FoundView(
    pokemon: PokemonEntity,
    modifier: Modifier = Modifier,
    onViewDetailsClick: () -> Unit,
    onContinueSearchingClick: () -> Unit
) {
    val pokemonColor = getColorForType(pokemon.types.first().type.name)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier
                .offset(y = (-40).dp)
                .widthIn(max = 400.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = pokemonColor.copy(alpha = 0.25f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = pokemon.localImagePath ?: pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(180.dp)
                )
                Text(pokemon.name, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pokemon.types.forEach { typeEntry ->
                        TypeChip(typeName = typeEntry.type.name)
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = onContinueSearchingClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seguir Buscando")
                    }
                    OutlinedButton(
                        onClick = onViewDetailsClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Detalles")
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeChip(typeName: String) {
    val backgroundColor = getColorForType(typeName)
    val textColor = getTextColorForType(backgroundColor)
    Surface(
        shape = CircleShape,
        color = backgroundColor,
        contentColor = textColor,
        tonalElevation = 2.dp
    ) {
        Text(
            text = typeName.replaceFirstChar { it.titlecase(Locale.ROOT) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun IdleView(viewModel: EncounterViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Text("Camina para encontrar Pokémon...", fontSize = 18.sp, textAlign = TextAlign.Center)
        Text("o", style = MaterialTheme.typography.bodySmall)
        Button(onClick = { viewModel.searchForRandomPokemon() }) {
            Icon(Icons.Default.Refresh, contentDescription = "Buscar Manualmente")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Buscar ahora")
        }
    }
}

@Composable
private fun SearchingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
        Text("Buscando un Pokémon...", fontSize = 18.sp)
    }
}

@Composable
private fun ErrorView(message: String, viewModel: EncounterViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Text(message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        Button(onClick = { viewModel.searchForRandomPokemon() }) {
            Text("Intentar de nuevo")
        }
    }
}

@Composable
private fun OfflineMessage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Icon(Icons.Default.WifiOff, contentDescription = "Offline", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.error)
        Text("Esta función requiere conexión a internet.", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}

@Composable
private fun PermissionMessage(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Text("Se requiere permiso de ubicación para encontrar Pokémon mientras caminas.", textAlign = TextAlign.Center)
        Button(onClick = onClick) {
            Text("Otorgar Permiso")
        }
    }
}

private fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(500)
    }
}