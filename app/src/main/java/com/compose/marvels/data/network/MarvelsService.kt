package com.compose.marvels.data.network

import com.compose.marvels.data.network.responses.CharacterDataWrapper
import com.compose.marvels.data.network.responses.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelsService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CharacterDataWrapper

    @GET("v1/public/characters/{characterId}")
    suspend fun getDetailCharacterById(
        @Path("characterId") characterId: Int
    ): CharacterDataWrapper

    @GET("v1/public/characters/{characterId}/comics")
    suspend fun getComicsCharacterById(
        @Path("characterId") characterId: Int
    ): ComicDataWrapper
}