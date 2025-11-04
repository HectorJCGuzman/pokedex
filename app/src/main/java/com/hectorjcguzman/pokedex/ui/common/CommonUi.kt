package com.hectorjcguzman.pokedex.ui.common
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexTopAppBar(isOnline: Boolean) {
    CenterAlignedTopAppBar(
        title = {
            Text("Pokédex", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.CatchingPokemon,
                contentDescription = "Pokédex Icon",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                val statusColor = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                Icon(
                    imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = "Connection Status",
                    tint = statusColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    fontSize = 12.sp,
                    color = statusColor
                )
            }
        }
    )
}