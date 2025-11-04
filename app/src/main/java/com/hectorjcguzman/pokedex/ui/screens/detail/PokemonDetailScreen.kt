package com.hectorjcguzman.pokedex.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import com.hectorjcguzman.pokedex.ui.utils.PokemonStatsCalculator
import com.hectorjcguzman.pokedex.ui.utils.getColorForType
import com.hectorjcguzman.pokedex.ui.utils.getTextColorForType
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is PokemonDetailState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PokemonDetailState.Success -> {
            val pokemon = state.pokemon
            val pokemonColor = getColorForType(pokemon.types.first().type.name)
            val scrollState = rememberScrollState()
            val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                DetailTopBar(
                    navController = navController,
                    isFavorite = pokemon.isFavorite,
                    onToggleFavorite = { viewModel.toggleFavorite() }
                )

                if (isCompact) {
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        DetailHeroSection(pokemon = pokemon, pokemonColor = pokemonColor)
                        PokemonNameAndType(pokemon = pokemon)
                        Spacer(modifier = Modifier.height(24.dp))
                        CompactLayout(pokemon = pokemon)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    ExpandedLayout(pokemon, pokemonColor)
                }
            }
        }
        is PokemonDetailState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
fun DetailTopBar(
    navController: NavController,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .zIndex(1f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = { navController.popBackStack() },
            shape = CircleShape,
            tonalElevation = 4.dp,
            shadowElevation = 2.dp
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.padding(8.dp)
            )
        }
        Surface(
            onClick = onToggleFavorite,
            shape = CircleShape,
            tonalElevation = 4.dp,
            shadowElevation = 2.dp
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else LocalContentColor.current,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun DetailHeroSection(pokemon: PokemonEntity, pokemonColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 64.dp)
            .height(300.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .background(
                    color = pokemonColor.copy(alpha = 0.6f),
                    shape = CircleShape
                )
        )
        AsyncImage(
            model = pokemon.localImagePath ?: pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier.size(250.dp)
        )
    }
}

@Composable
fun PokemonNameAndType(pokemon: PokemonEntity, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "#%03d".format(pokemon.id),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp
        )
        Text(
            text = pokemon.name,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.types.forEach { typeEntry ->
                TypeChip(typeName = typeEntry.type.name)
            }
        }
    }
}

@Composable
fun PhysicalAttributesCard(pokemon: PokemonEntity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AttributeItem("Height", "${pokemon.height / 10.0} m", Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            AttributeItem("Weight", "${pokemon.weight / 10.0} kg", Modifier.weight(1f))
        }
    }
}

@Composable
fun AttributeItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun BaseStatsCard(pokemon: PokemonEntity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Base Stats",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            pokemon.stats.forEach { statEntry ->
                StatBar(
                    statName = statEntry.stat.name,
                    statValue = statEntry.baseStat
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StatBar(statName: String, statValue: Int) {
    val maxStat = PokemonStatsCalculator.calculateMaxStat(statValue, statName)
    val statPercentage = statValue.toFloat() / maxStat.toFloat()
    val offensiveStats = listOf("attack", "special-attack", "speed")
    val statColor = if (offensiveStats.contains(statName)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.replaceFirstChar { it.titlecase(Locale.ROOT) }.replace("-", " "),
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp
        )
        Text(
            text = statValue.toString(),
            modifier = Modifier.width(40.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(statPercentage)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(statColor)
            )
        }
    }
}

@Composable
fun CompactLayout(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PhysicalAttributesCard(pokemon = pokemon)
        BaseStatsCard(pokemon = pokemon)
    }
}

@Composable
fun ExpandedLayout(pokemon: PokemonEntity, pokemonColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailHeroSection(pokemon = pokemon, pokemonColor = pokemonColor)
            PokemonNameAndType(pokemon = pokemon)
            Spacer(modifier = Modifier.height(24.dp))
            PhysicalAttributesCard(pokemon = pokemon)
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            BaseStatsCard(pokemon = pokemon)
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
        contentColor = textColor
    ) {
        Text(
            text = typeName.replaceFirstChar { it.titlecase(Locale.ROOT) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            fontSize = 14.sp
        )
    }
}