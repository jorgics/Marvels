package com.compose.marvels.data.network

import android.util.Log
import com.compose.marvels.domain.Repository
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.GalleryModel
import javax.inject.Inject

class MarvelsRepository @Inject constructor(private val apiService: MarvelsService) : Repository {
    override suspend fun getCharacters(limit: Int, offset: Int): GalleryModel? {
        runCatching { apiService.getCharacters(limit, offset) }
            .onSuccess { return GalleryModel(it.data?.total, it.data?.characters?.map { character -> character.toDomain() }) }
            .onFailure { Log.e("Error", "${it.cause} ${it.message}") }
        return null
    }

    override suspend fun getDetailCharacterById(characterID: Int): CharacterModel? {
        runCatching { apiService.getDetailCharacterById(characterID) }
            .onSuccess { return it.data?.characters?.first()?.toDomain() }
            .onFailure { Log.e("Error", "${it.cause} ${it.message}") }
        return null
    }

    override suspend fun getComicsCharacterById(characterID: Int): List<ComicModel>? {
        runCatching { apiService.getComicsCharacterById(characterID) }
            .onSuccess { return it.data?.comics?.map { comic -> comic.toDomain() } }
            .onFailure { Log.e("Error", "${it.cause} ${it.message}") }
        return null
    }
}