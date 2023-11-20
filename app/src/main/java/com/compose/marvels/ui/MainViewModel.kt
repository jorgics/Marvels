package com.compose.marvels.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.GalleryModel
import com.compose.marvels.domain.usecases.GetCharactersByNameStartsWithUseCase
import com.compose.marvels.domain.usecases.GetCharactersUseCase
import com.compose.marvels.domain.usecases.GetComicsByIdUseCase
import com.compose.marvels.domain.usecases.GetDetailCharacterById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getComicsByIdUseCase: GetComicsByIdUseCase,
    private val getDetailCharacterById: GetDetailCharacterById,
    private val getCharactersByNameStartsWith: GetCharactersByNameStartsWithUseCase
) : ViewModel() {

    companion object{
        private const val LIMIT = 20
    }

    private val _characters = MutableStateFlow<List<CharacterModel>>(emptyList())

    private val _charactersList = MutableStateFlow<List<CharacterModel>>(emptyList())
    val charactersList: StateFlow<List<CharacterModel>> = _charactersList

    private val _character = MutableStateFlow<CharacterModel?>(null)
    val character: StateFlow<CharacterModel?> = _character

    private val _comics = MutableStateFlow<List<ComicModel>>(emptyList())
    val comics: StateFlow<List<ComicModel>> = _comics

    private val _total = MutableStateFlow(0)

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText

    private val _mode = MutableStateFlow(false)
    val mode: StateFlow<Boolean> = _mode

    private val _page = MutableStateFlow(-1)

    fun getPage() = _page.value++

    fun onModeChange() {
        _mode.value = !_mode.value
    }

    fun onValueChange(filterText: String) {
        viewModelScope.launch {
            _filterText.update { filterText }
            if (filterText.isNotEmpty()) filterList()
            else _charactersList.value = _characters.value.sortedBy { it.name }
        }
    }

    private fun filterList() {
        viewModelScope.launch {
            _charactersList.value = _characters.value.filter {
                it.name!!.lowercase().contains(_filterText.value.lowercase())
            }
        }
    }

    fun onIconClick() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val galleryModel = getCharactersByNameStartsWith.invoke(ParamsDto(nameStartsWith = _filterText.value))
                setCharacters(galleryModel)
                _isLoading.value = false
            } catch (e: Exception) {
                error()
            }
        }
    }

    fun getCharacters(page: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val galleryModel = getCharactersUseCase.invoke(ParamsDto(offset = page * LIMIT))
                _total.value = galleryModel.total ?: 0
                setCharacters(galleryModel)
                _isLoading.value = false
            } catch (e: Exception) {
                error()
            }

        }
    }

    private fun setCharacters(galleryModel: GalleryModel) {
        _characters.value = galleryModel.characters ?: emptyList()
        _charactersList.value = (_charactersList.value + _characters.value).distinct().sortedBy { it.name }
        _characters.value = _charactersList.value
    }

    private fun getDetailById(characterId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _character.value = getDetailCharacterById.invoke(characterId)
            } catch (e: Exception) {
                error()
            }

        }
    }

    private fun getComicsById(characterId: Int) {
        viewModelScope.launch {
            try {
                _comics.value = getComicsByIdUseCase.invoke(characterId) ?: emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                error()
            }
        }
    }

    private fun error() {
        viewModelScope.launch {
            _isLoading.value = false
            _isError.value = true
            delay(3000)
            _isError.value = false
        }
    }

    private fun getAllDetails(characterId: Int) {
        getDetailById(characterId)
        getComicsById(characterId)
    }

    fun onItemClick(characterModel: CharacterModel) {
        getAllDetails(characterModel.characterID!!)
        _filterText.value = ""
        _charactersList.value = _characters.value.sortedBy { it.name }
    }

    fun isReachedEnd(lazyGridState: LazyGridState): Boolean =
        lazyGridState.firstVisibleItemIndex + lazyGridState.layoutInfo.visibleItemsInfo.size >= lazyGridState.layoutInfo.totalItemsCount

}