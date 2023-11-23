package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.responses.Image
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.DetailModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date


class GetComicsByIdUseCaseTest{

    @RelaxedMockK
    private lateinit var repository: MarvelsRepository

    lateinit var getComicsByIdUseCase: GetComicsByIdUseCase
    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getComicsByIdUseCase = GetComicsByIdUseCase(repository)
    }

    @Test
    fun `when the marvels service have characterId negative return null`() = runBlocking {
        val characterID = -1
        //Given
        coEvery { repository.getComicsCharacterById(characterID) } returns DetailModel()
        //When
        val response = getComicsByIdUseCase(characterID)
        //Then
        coVerify(exactly = 1) { repository.getComicsCharacterById(characterID) }
        assert(response == DetailModel())
    }

    @Test
    fun `when the marvels service have characterId good but dont have comics and return emptyList`() = runBlocking {
        val characterID = 1011334
        val detailModel = DetailModel(comics = emptyList())
        //Given
        coEvery { repository.getComicsCharacterById(characterID) } returns detailModel
        //When
        val response = getComicsByIdUseCase(characterID)
        //Then
        coVerify(exactly = 1) { repository.getComicsCharacterById(characterID) }
        assert(response.comics == detailModel.comics)
    }

    @Test
    fun `when the marvels service have characterId good return list`() = runBlocking {
        val characterID = 1017100
        val detailModel = DetailModel(comics = listOf(ComicModel("FREE COMIC BOOK DAY 2013 1 (2013) #1", Image("","jpg"), Date())))
        //Given
        coEvery { repository.getComicsCharacterById(characterID) } returns detailModel
        //When
        val response = getComicsByIdUseCase(characterID)
        //Then
        coVerify(exactly = 1) { repository.getComicsCharacterById(characterID) }
        assert(response.comics != null)
        assert(response.comics?.first()?.title == detailModel.comics?.first()?.title)
    }
}