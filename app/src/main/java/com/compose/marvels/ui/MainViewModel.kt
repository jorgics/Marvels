package com.compose.marvels.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.viewModelScope
import com.compose.marvels.core.base.viewmodel.BaseViewModel
import com.compose.marvels.core.utils.CodesManager
import com.compose.marvels.core.utils.MessagesErrorManager
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.domain.models.Result
import com.compose.marvels.domain.usecases.GetCharactersByNameStartsWithUseCase
import com.compose.marvels.domain.usecases.GetCharactersUseCase
import com.compose.marvels.domain.usecases.GetComicsByIdUseCase
import com.compose.marvels.domain.usecases.GetDetailCharacterById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getComicsByIdUseCase: GetComicsByIdUseCase,
    private val getDetailCharacterById: GetDetailCharacterById,
    private val getCharactersByNameStartsWith: GetCharactersByNameStartsWithUseCase
) : BaseViewModel() {

    companion object{
        private const val LIMIT = 20
        private var _charactersTotal: List<CharacterModel> = emptyList()
        private var _page = -1
        private var _total = 0
    }

    private val _charactersList = MutableStateFlow<List<CharacterModel>>(emptyList())
    val charactersList: StateFlow<List<CharacterModel>> = _charactersList

    private val _character = MutableStateFlow<CharacterModel?>(null)
    val character: StateFlow<CharacterModel?> = _character

    private val _comics = MutableStateFlow<List<ComicModel>>(emptyList())
    val comics: StateFlow<List<ComicModel>> = _comics

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText

    private val _mode = MutableStateFlow(false)
    val mode: StateFlow<Boolean> = _mode

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded

    private val _animate = MutableStateFlow(false)
    val animate: StateFlow<Boolean> = _animate

    private val _passwordVisibility = MutableStateFlow(false)
    val passwordVisibility: StateFlow<Boolean> = _passwordVisibility

    fun getPage() = _page++

    fun onVisibilityChange() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    fun onAnimateChange() {
        _animate.value = !animate.value
    }

    fun onModeChange() {
        _mode.value = !_mode.value
    }

    fun onValueChange(filterText: String) {
        viewModelScope.launch {
            _filterText.value = filterText
            if (filterText.isNotEmpty()) filterList()
            else _charactersList.value = _charactersTotal.sortedBy { it.name }
        }
    }

    private fun filterList() {
        viewModelScope.launch {
            _charactersList.value = _charactersTotal.filter {
                it.name!!.lowercase().contains(_filterText.value.lowercase())
            }
        }
    }

    fun onIconClick() {
        viewModelScope.launch {
            try {
                setLoading(true)
                val galleryModel = getCharactersByNameStartsWith.invoke(ParamsDto(nameStartsWith = _filterText.value))
                setCharacters(galleryModel.characters)
                setLoading(false)
            } catch (e: Exception) {
                errorException()
            }
        }
    }

    fun onCleanClick() {
        viewModelScope.launch {
            _filterText.value = ""
            _charactersList.value = _charactersTotal.sortedBy { it.name }
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            try {
                setLoading(true)
                val galleryModel = getCharactersUseCase.invoke(ParamsDto())
                _total = galleryModel.total ?: 0
                getErrorMessage(galleryModel.error)
                setCharacters(galleryModel.characters)
                setLoading(false)
            } catch (e: Exception) {
                errorException()
            }

        }
    }

    fun nextCharacters(page: Int) {
        viewModelScope.launch {
            try {
                val galleryModel = getCharactersUseCase.invoke(ParamsDto(offset = nextPage(page)))
                getErrorMessage(galleryModel.error)
                setCharacters(galleryModel.characters)
            } catch (e: Exception) {
                errorException()
            }

        }
    }

    private fun nextPage(page: Int): Int = (page * LIMIT) % _total

    private fun setCharacters(characters: List<CharacterModel>?) {
        val list = characters ?: emptyList()
        _charactersList.value = (_charactersList.value + list).distinct().sortedBy { it.name }
        setCharactersTotal(_charactersList.value)
    }

    private fun setCharactersTotal(list: List<CharacterModel>) {
        _charactersTotal = (_charactersTotal + list).distinct().sortedBy { it.name }
        if (_charactersTotal.isEmpty()) _page = -1
    }

    private fun getComicsById(characterId: Int) {
        viewModelScope.launch {
            try {
                val detailModel = getComicsByIdUseCase.invoke(characterId)
                _comics.value = detailModel.comics ?: emptyList()
                getErrorMessage(detailModel.error)
                setLoading(false)
            } catch (e: Exception) {
                errorException()
            }
        }
    }

    private fun getDetailCharacterById(characterId: Int) {
        viewModelScope.launch {
            try {
                val detailModel = getDetailCharacterById.invoke(characterId)
                _character.value = detailModel.characterModel
                getErrorMessage(detailModel.error)
                setLoading(false)
            } catch (e: Exception) {
                errorException()
            }
        }
    }

    private fun errorException() {
        viewModelScope.launch {
            setLoading(false)
            _isError.value = true
            delay(3000)
            _isError.value = false
        }
    }

    private fun getErrorMessage(error: Result?) {
        if (error != null) {
            when(error.code) {
                CodesManager.CODE_409 -> {
                    _errorMessage.value = MessagesErrorManager.MISSING_API_KEY_HASH_TIMESTAMP
                    errorException()
                }
                CodesManager.CODE_405 -> {
                    _errorMessage.value = MessagesErrorManager.METHOD_NOT_ALLOWED
                    errorException()
                }
                CodesManager.CODE_403 -> {
                    _errorMessage.value = MessagesErrorManager.FORBIDDEN
                    errorException()
                }
                CodesManager.CODE_401 -> {
                    _errorMessage.value = MessagesErrorManager.INVALID
                    errorException()
                }
                else -> { _errorMessage.value = "" }
            }
        }
    }

    fun onItemClick(characterModel: CharacterModel) {
        onAnimateChange()
        getDetailCharacterById(characterModel.characterID!!)
        getComicsById(characterModel.characterID)
        _filterText.value = ""
        _charactersList.value = _charactersTotal.sortedBy { it.name }
    }

    fun isReachedEnd(lazyGridState: LazyGridState): Boolean =
        lazyGridState.firstVisibleItemIndex + lazyGridState.layoutInfo.visibleItemsInfo.size >= lazyGridState.layoutInfo.totalItemsCount

    fun onExpanded() {
        _expanded.value = !_expanded.value
    }

    fun reset() {
        _page = -1
        _total = 0
        _charactersTotal = emptyList()
        _animate.value = false
        _expanded.value = false
        _isError.value = false
        setLoading(false)
        _filterText.value = ""
    }

}