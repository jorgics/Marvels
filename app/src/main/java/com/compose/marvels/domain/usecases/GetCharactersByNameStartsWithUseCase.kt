package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.Repository
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.GalleryModel
import javax.inject.Inject

class GetCharactersByNameStartsWithUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(paramsDto: ParamsDto) = repository.getCharactersByNameStartsWith(paramsDto)
}