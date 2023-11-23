package com.compose.marvels.data.network

import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.Repository
import com.compose.marvels.domain.models.DetailModel
import com.compose.marvels.domain.models.GalleryModel
import com.compose.marvels.domain.models.Result
import javax.inject.Inject

class MarvelsRepository @Inject constructor(private val apiService: MarvelsService) : Repository {
    override suspend fun getCharacters(paramsDto: ParamsDto): GalleryModel {
        val response = apiService.getCharacters(paramsDto.limit, paramsDto.offset)
        return if (response.isSuccessful) {
            GalleryModel(
                response.body()?.data?.total,
                response.body()?.data?.characters?.map { character -> character.toDomain() })
        } else {
            GalleryModel(
                error = Result(
                    message = response.message(),
                    code = response.code()
                )
            )
        }
    }

    override suspend fun getCharactersByNameStartsWith(paramsDto: ParamsDto): GalleryModel {

        val response = apiService.getCharactersByNameStartsWith(
            paramsDto.limit,
            paramsDto.offset,
            paramsDto.nameStartsWith
        )

        return if (response.isSuccessful) {
            GalleryModel(characters = response.body()?.data?.characters?.map { character -> character.toDomain() })
        } else {
            GalleryModel(
                error = Result(
                    message = response.message(),
                    code = response.code()
                )
            )
        }
    }

    override suspend fun getDetailCharacterById(characterID: Int): DetailModel {
        val response = apiService.getDetailCharacterById(characterID)
        return if (response.isSuccessful) {
            DetailModel(characterModel = response.body()?.data?.characters?.first()?.toDomain())
        } else {
            DetailModel(
                error = Result(
                    message = response.message(),
                    code = response.code()
                )
            )
        }
    }

    override suspend fun getComicsCharacterById(characterID: Int): DetailModel {
        val response = apiService.getComicsCharacterById(characterID)
        return if (response.isSuccessful) {
            DetailModel(comics = response.body()?.data?.comics?.map { comic ->
                comic.toDomain()
            })
        } else {
            DetailModel(
                error = Result(
                    message = response.message(),
                    code = response.code()
                )
            )
        }
    }
}