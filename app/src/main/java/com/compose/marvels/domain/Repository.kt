package com.compose.marvels.domain

import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.GalleryModel

interface Repository {
    suspend fun getCharacters(limit: Int, offset: Int): GalleryModel?
    suspend fun getDetailCharacterById(characterID: Int): CharacterModel?
    suspend fun getComicsCharacterById(characterID: Int): List<ComicModel>?
}