package com.compose.marvels.domain.usecases

import com.compose.marvels.domain.Repository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(limit: Int, offset: Int) = repository.getCharacters(limit, offset)
}