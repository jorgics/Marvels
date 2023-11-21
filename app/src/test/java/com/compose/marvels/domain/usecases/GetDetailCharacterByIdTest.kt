package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.responses.ComicList
import com.compose.marvels.data.network.responses.Image
import com.compose.marvels.domain.models.CharacterModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetDetailCharacterByIdTest{

    @RelaxedMockK
    private lateinit var repository: MarvelsRepository

    lateinit var getDetailCharacterById: GetDetailCharacterById
    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getDetailCharacterById = GetDetailCharacterById(repository)
    }

    @Test
    fun `when the marvels service have characterId negative return null`() = runBlocking {
        val characterId = -1
        //Given
        coEvery { repository.getDetailCharacterById(characterId) } returns null
        //When
        val response = getDetailCharacterById(characterId)
        //Then
        coVerify(exactly = 1) { repository.getDetailCharacterById(characterId) }
        assert(response == null)
    }

    @Test
    fun `when the marvels service have characterId good return character`() = runBlocking {
        val characterId = 1011334
        val characterModel = CharacterModel(
            characterID = 1011334,
            name = "3-D Man",
            description = "",
            image = Image("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","jpg"),
            comics = ComicList(null, null, null , emptyList())

        )
        //Given
        coEvery { repository.getDetailCharacterById(characterId) } returns characterModel
        //When
        val response = getDetailCharacterById(characterId)
        //Then
        coVerify(exactly = 1) { repository.getDetailCharacterById(characterId) }
        assert(response?.characterID == characterModel.characterID)
        assert(response?.name == characterModel.name)
        assert(response?.comics != null)
        assert(response?.image == characterModel.image)
        assert(response?.description == characterModel.description)
    }
}