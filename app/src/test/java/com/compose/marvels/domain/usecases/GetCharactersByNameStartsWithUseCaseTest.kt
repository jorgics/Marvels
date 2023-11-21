package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.CharacterModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCharactersByNameStartsWithUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: MarvelsRepository

    lateinit var getCharactersByNameStartsWithUseCase: GetCharactersByNameStartsWithUseCase
    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getCharactersByNameStartsWithUseCase = GetCharactersByNameStartsWithUseCase(repository)
    }

    @Test
    fun `when the characters are filter by name start with and doesnt return anything`() = runBlocking {
        val paramsDto = ParamsDto(nameStartsWith = "asdasfa")
        //Given
        coEvery { repository.getCharactersByNameStartsWith(paramsDto) } returns emptyList()
        //When
        val response = getCharactersByNameStartsWithUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharactersByNameStartsWith(paramsDto) }
        assert(emptyList<CharacterModel>() == response)
    }

    @Test
    fun `when the characters are filter by name start with and return characters filtered`() = runBlocking {
        val paramsDto = ParamsDto(nameStartsWith = "Spider")
        val list = listOf(CharacterModel(), CharacterModel())
        //Given
        coEvery { repository.getCharactersByNameStartsWith(paramsDto) } returns list
        //When
        val response = getCharactersByNameStartsWithUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharactersByNameStartsWith(paramsDto) }
        assert(list == response)
    }

    @Test
    fun `when the characters are filter by name exactly and return character`() = runBlocking {
        val paramsDto = ParamsDto(nameStartsWith = "Spider-Man (Peter Parker)")
        val list = listOf(CharacterModel())
        //Given
        coEvery { repository.getCharactersByNameStartsWith(paramsDto) } returns list
        //When
        val response = getCharactersByNameStartsWithUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharactersByNameStartsWith(paramsDto) }
        assert(list == response)
    }
}