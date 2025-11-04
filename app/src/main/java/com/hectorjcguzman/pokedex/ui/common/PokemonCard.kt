package com.hectorjcguzman.pokedex.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import com.hectorjcguzman.pokedex.ui.utils.getColorForType

@Composable
fun PokemonCard(
    pokemon: PokemonEntity,
    onClick: () -> Unit
) {
    val pokemonColor = getColorForType(pokemon.types.first().type.name)
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundAlpha = if (isDarkTheme) 0.6f else 0.8f
    val footerAlpha = if (isDarkTheme) 0.7f else 0.7f

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .clip(RoundedCornerShape(12.dp))
                .background(pokemonColor.copy(alpha = backgroundAlpha))
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.surface.copy(alpha = footerAlpha),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text(
                        text = pokemon.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        AsyncImage(
            model = pokemon.localImagePath ?: pokemon.imageUrl,
            contentDescription = "Image of ${pokemon.name}",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.Center)
                .offset(y = (-35).dp)
        )

        if (pokemon.wasEncountered) {
            Icon(
                imageVector = Icons.Default.TravelExplore,
                contentDescription = "Encontrado aleatoriamente",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
                    .padding(4.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun SkeletonCard() {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
        }
    }
}