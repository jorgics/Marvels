package com.compose.marvels.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.compose.marvels.data.network.responses.ComicList
import com.compose.marvels.data.network.responses.ComicSummary
import com.compose.marvels.data.network.responses.Image
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.usecases.GetCharactersUseCase
import com.compose.marvels.domain.usecases.GetComicsByIdUseCase
import com.compose.marvels.domain.usecases.GetDetailCharacterById
import com.compose.marvels.ui.models.CharactersPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getComicsByIdUseCase: GetComicsByIdUseCase,
    private val getDetailCharacterById: GetDetailCharacterById
) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterModel>>(emptyList())
    val characters: StateFlow<List<CharacterModel>> = _characters

    private val _filterList = MutableStateFlow<List<CharacterModel>>(emptyList())
    val filterList: StateFlow<List<CharacterModel>> = _filterList

    private val _character = MutableStateFlow<CharacterModel?>(null)
    val character: StateFlow<CharacterModel?> = _character

    private val _comics = MutableStateFlow<List<ComicModel>>(emptyList())
    val comics: StateFlow<List<ComicModel>> = _comics

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText

    val lazyPagingItems: Flow<PagingData<CharacterModel>> = Pager(
        pagingSourceFactory = { CharactersPagingSource(getCharactersUseCase) },
        config = PagingConfig(pageSize = 20)
    ).flow.cachedIn(viewModelScope)

    fun onValueChange(filterText: String) {
        viewModelScope.launch {
            _filterText.update { filterText }
            filterList()
        }
    }

    fun filterList() {
        viewModelScope.launch {
            _filterList.value = _characters.value.filter { it.name!!.lowercase().contains(_filterText.value.lowercase()) }
        }
    }
    fun getCharacters() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val galleryModel = getCharactersUseCase.invoke(20, 0)
                _characters.value = galleryModel!!.characters ?: emptyList()
                _filterList.value = _characters.value
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                delay(3000)
                _isError.value = false
            }

        }
    }

    fun getDetailById(characterId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _character.value = getDetailCharacterById.invoke(characterId)
            } catch (e: Exception) {
                _isLoading.value = false
                _isError.value = true
                delay(3000)
                _isError.value = false
            }

        }
    }

    fun getComicsById(characterId: Int) {
        viewModelScope.launch {
            try {
                _comics.value = getComicsByIdUseCase.invoke(characterId) ?: emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _isError.value = true
                delay(3000)
                _isError.value = false

            }
        }
    }

    fun getAllDetails(characterId: Int) {
        getDetailById(characterId)
        getComicsById(characterId)
    }

    fun onItemClick(characterModel: CharacterModel) {
        getAllDetails(characterModel.characterID!!)
        _filterText.value = ""
    }

    fun isReachedEnd(lazyGridState: LazyGridState): Boolean =
        lazyGridState.firstVisibleItemIndex + lazyGridState.layoutInfo.visibleItemsInfo.size >= lazyGridState.layoutInfo.totalItemsCount

}