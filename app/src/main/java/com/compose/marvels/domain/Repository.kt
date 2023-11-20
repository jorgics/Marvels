package com.compose.marvels.domain

import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.GalleryModel

interface Repository {
    suspend fun getCharacters(paramsDto: ParamsDto): GalleryModel?

    suspend fun getCharactersByNameStartsWith(paramsDto: ParamsDto): GalleryModel?
    suspend fun getDetailCharacterById(characterID: Int): CharacterModel?
    suspend fun getComicsCharacterById(characterID: Int): List<ComicModel>?
}