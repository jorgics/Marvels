package com.compose.marvels.domain.usecases

import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.responses.ComicList
import com.compose.marvels.data.network.responses.Image
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.DetailModel
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
        val detailModel = DetailModel()
        //Given
        coEvery { repository.getDetailCharacterById(characterId) } returns DetailModel()
        //When
        val response = getDetailCharacterById(characterId)
        //Then
        coVerify(exactly = 1) { repository.getDetailCharacterById(characterId) }
        assert(response == DetailModel())
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
        val detailModel = DetailModel(characterModel = characterModel)
        //Given
        coEvery { repository.getDetailCharacterById(characterId) } returns detailModel
        //When
        val response = getDetailCharacterById(characterId)
        //Then
        coVerify(exactly = 1) { repository.getDetailCharacterById(characterId) }
        assert(response.characterModel?.characterID == detailModel.characterModel?.characterID)
        assert(response.characterModel?.name == detailModel.characterModel?.name)
        assert(response.characterModel?.comics != null)
        assert(response.characterModel?.image == detailModel.characterModel?.image)
        assert(response.characterModel?.description == detailModel.characterModel?.description)
    }
}