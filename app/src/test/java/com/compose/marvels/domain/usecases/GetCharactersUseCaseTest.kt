package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.GalleryModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetCharactersUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: MarvelsRepository

    lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getCharactersUseCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `when the marvels service have limit more 100 doesnt return null`() = runBlocking {
        val paramsDto = ParamsDto(limit = 101)
        //Given
        coEvery { repository.getCharacters(paramsDto) } returns GalleryModel()
        //When
        val response = getCharactersUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharacters(paramsDto) }
        assert(response.characters == null)
        assert(response.total == null)
    }

    @Test
    fun `when the characters have limit negative return null`() = runBlocking {
        val paramsDto = ParamsDto(limit = -1)
        //Given
        coEvery { repository.getCharacters(paramsDto) } returns GalleryModel()
        //When
        val response = getCharactersUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharacters(paramsDto) }
        assert(response.characters == null)
        assert(response.total == null)
    }

    @Test
    fun `when the characters have limit range 0-100 return characters`() = runBlocking {
        val paramsDto = ParamsDto(limit = 20)
        //Given
        coEvery { repository.getCharacters(paramsDto) } returns GalleryModel(
            characters = listOf(),
            total = 1560
        )
        //When
        val response = getCharactersUseCase(paramsDto)
        //Then
        coVerify(exactly = 1) { repository.getCharacters(paramsDto) }
        assert(response.characters != null)
        assert(response.total != null)
    }
}