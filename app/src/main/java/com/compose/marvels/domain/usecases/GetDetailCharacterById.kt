package com.compose.marvels.domain.usecases

import com.compose.marvels.domain.Repository
import javax.inject.Inject

class GetDetailCharacterById @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(characterId: Int) = repository.getDetailCharacterById(characterId)
}
