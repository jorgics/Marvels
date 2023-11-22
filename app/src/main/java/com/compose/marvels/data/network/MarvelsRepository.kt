package com.compose.marvels.data.network

import android.util.Log
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.Repository
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.GalleryModel
import javax.inject.Inject

class MarvelsRepository @Inject constructor(private val apiService: MarvelsService) : Repository {
    override suspend fun getCharacters(paramsDto: ParamsDto): GalleryModel? {
        runCatching { apiService.getCharacters(paramsDto.limit, paramsDto.offset) }
            .onSuccess { return GalleryModel(it.data?.total, it.data?.characters?.map { character -> character.toDomain() }) }
            .onFailure {
                Log.e("Error", "${it.message}")
                return null
            }
        return null
    }

    override suspend fun getCharactersByNameStartsWith(paramsDto: ParamsDto): List<CharacterModel>? {
        runCatching { apiService.getCharactersByNameStartsWith(paramsDto.limit, paramsDto.offset, paramsDto.nameStartsWith) }
            .onSuccess { return it.data?.characters?.map { character -> character.toDomain() } }
            .onFailure {
                Log.e("Error", "${it.cause} ${it.message}")
                return null
            }
        return null
    }

    override suspend fun getDetailCharacterById(characterID: Int): CharacterModel? {
        runCatching { apiService.getDetailCharacterById(characterID) }
            .onSuccess { return it.data?.characters?.first()?.toDomain() }
            .onFailure {
                Log.e("Error", "${it.cause} ${it.message}")
                return null
            }
        return null
    }

    override suspend fun getComicsCharacterById(characterID: Int): List<ComicModel>? {
        runCatching { apiService.getComicsCharacterById(characterID) }
            .onSuccess { return it.data?.comics?.map { comic -> comic.toDomain() } }
            .onFailure {
                Log.e("Error", "${it.cause} ${it.message}")
                return null
            }
        return null
    }
}