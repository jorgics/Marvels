package com.compose.marvels.data.network.responses

import com.compose.marvels.domain.models.CharacterModel
import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val characterID: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("thumbnail")
    val image: Image?,
    @SerializedName("comics")
    val comics: ComicList?
) {
    fun toDomain(): CharacterModel = CharacterModel(characterID, name, description, image, comics)
}
