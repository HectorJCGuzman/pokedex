package com.hectorjcguzman.pokedex.data

import android.content.Context
import android.util.Log
import com.hectorjcguzman.pokedex.data.local.PokemonDao
import com.hectorjcguzman.pokedex.data.local.PokemonEntity
import com.hectorjcguzman.pokedex.data.model.PokemonDetails
import com.hectorjcguzman.pokedex.data.network.PokeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.random.Random

class PokemonRepositoryImpl(
    private val pokeApiService: PokeApiService,
    private val pokemonDao: PokemonDao,
    private val context: Context
) : PokemonRepository {

    override val allPokemon: Flow<List<PokemonEntity>> = pokemonDao.getAllPokemon()
    override val favoritePokemon: Flow<List<PokemonEntity>> = pokemonDao.getFavoritePokemon()

    override suspend fun fetchAndSavePokemon(offset: Int, limit: Int) {
        try {
            val currentCount = pokemonDao.getSequentialPokemonCount()
            if (currentCount < offset + limit) {
                val listResponse = pokeApiService.getPokemonList(offset = offset, limit = limit)
                val detailsList = listResponse.results.mapNotNull {
                    try {
                        pokeApiService.getPokemonDetails(it.name)
                    } catch (e: Exception) {
                        null
                    }
                }

                val entities = detailsList.map { details ->
                    val localPath = saveImageToInternalStorage(details.sprites.other.officialArtwork.frontDefault, "${details.id}.png")
                    details.toEntity(localPath)
                }
                pokemonDao.insertAll(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun findRandomPokemon(): Result<EncounterResult> {
        return try {
            val totalPokemon = 1025
            val randomId = Random.nextInt(1, totalPokemon + 1)
            val pokemonDetails = pokeApiService.getPokemonDetailsById(randomId)

            val existingPokemon = pokemonDao.getPokemonById(randomId)
            if (existingPokemon != null) {
                Log.d("Encounter", "Pokemon #${existingPokemon.id} already exists.")
                Result.success(EncounterResult(pokemon = existingPokemon, isNew = false))
            } else {
                Log.d("Encounter", "Found new Pokemon #${pokemonDetails.id}.")
                val localPath = saveImageToInternalStorage(pokemonDetails.sprites.other.officialArtwork.frontDefault, "${pokemonDetails.id}.png")
                val newEntity = pokemonDetails.toEntity(localPath).copy(wasEncountered = true)
                pokemonDao.insert(newEntity)
                Result.success(EncounterResult(pokemon = newEntity, isNew = true))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(pokemonId: Int) {
        val pokemon = pokemonDao.getPokemonById(pokemonId)
        pokemon?.let {
            pokemonDao.updateFavoriteStatus(pokemonId, !it.isFavorite)
        }
    }

    override fun getPokemonById(pokemonId: Int): Flow<PokemonEntity?> = flow {
        emit(pokemonDao.getPokemonById(pokemonId))
    }

    private fun saveImageToInternalStorage(imageUrl: String, fileName: String): String? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream

            val directory = File(context.filesDir, "pokemon_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun PokemonDetails.toEntity(localPath: String?) = PokemonEntity(
        id = this.id,
        name = this.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
        height = this.height,
        weight = this.weight,
        types = this.types,
        imageUrl = this.sprites.other.officialArtwork.frontDefault,
        stats = this.stats,
        localImagePath = localPath
    )
}