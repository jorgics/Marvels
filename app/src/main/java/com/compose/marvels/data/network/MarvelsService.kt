package com.compose.marvels.data.network

import com.compose.marvels.data.network.responses.CharacterDataWrapper
import com.compose.marvels.data.network.responses.ComicDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelsService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<CharacterDataWrapper>

    @GET("v1/public/characters")
    suspend fun getCharactersByNameStartsWith(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("nameStartsWith") nameStartsWith: String = ""
    ): Response<CharacterDataWrapper>

    @GET("v1/public/characters/{characterId}")
    suspend fun getDetailCharacterById(
        @Path("characterId") characterId: Int
    ): Response<CharacterDataWrapper>

    @GET("v1/public/characters/{characterId}/comics")
    suspend fun getComicsCharacterById(
        @Path("characterId") characterId: Int
    ): Response<ComicDataWrapper>
}