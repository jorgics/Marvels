package com.compose.marvels.ui

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.marvels.core.utils.KeysManger
import com.compose.marvels.core.utils.SharedPreferenceManager
import com.compose.marvels.data.network.dtos.ParamsDto
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
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
) : ViewModel() {

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText

    private val _mode = MutableStateFlow(false)
    val mode: StateFlow<Boolean> = _mode

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded

    private val _animate = MutableStateFlow(false)
    val animate: StateFlow<Boolean> = _animate

    private val _isApiKey = MutableStateFlow(false)
    val isApiKey: StateFlow<Boolean> = _isApiKey

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey

    private val _privateKey = MutableStateFlow("")
    val privateKey: StateFlow<String> = _privateKey

    private val _passwordVisibility = MutableStateFlow(false)
    val passwordVisibility: StateFlow<Boolean> = _passwordVisibility

    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled

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

    private fun isEnabled() {
        _enabled.value = isValidApiKey() && isValidPrivateKey()
    }

    private fun isValidApiKey(): Boolean = _apiKey.value.isNotEmpty() && _apiKey.value.length > 20

    private fun isValidPrivateKey(): Boolean = _privateKey.value.isNotEmpty() && _privateKey.value.length > 30

    fun onSaveClick() {
        SharedPreferenceManager.setString(KeysManger.API_KEY, _apiKey.value)
        SharedPreferenceManager.setString(KeysManger.PRIVATE_KEY, _privateKey.value)
        _isApiKey.value = isApikeyAndPrivateKeyInPreference()
    }

    fun onApikeyChange(apikey: String) {
        _apiKey.value = apikey
        isEnabled()
    }

    fun onPrivateKeyChange(privateKey: String) {
        _privateKey.value = privateKey
        isEnabled()
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
                _isLoading.value = true
                val list = getCharactersByNameStartsWith.invoke(ParamsDto(nameStartsWith = _filterText.value))
                setCharacters(list)
                _isLoading.value = false
            } catch (e: Exception) {
                error()
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
                _isLoading.value = true
                val galleryModel = getCharactersUseCase.invoke(ParamsDto())
                _total = galleryModel.total ?: 0
                setCharacters(galleryModel.characters)
                _isLoading.value = false
            } catch (e: Exception) {
                error()
            }

        }
    }

    fun nextCharacters(page: Int) {
        viewModelScope.launch {
            try {
                val galleryModel = getCharactersUseCase.invoke(ParamsDto(offset = nextPage(page)))
                setCharacters(galleryModel.characters)
            } catch (e: Exception) {
                error()
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
        onAnimateChange()
        getAllDetails(characterModel.characterID!!)
        _filterText.value = ""
        _charactersList.value = _charactersTotal.sortedBy { it.name }
    }

    fun isReachedEnd(lazyGridState: LazyGridState): Boolean =
        lazyGridState.firstVisibleItemIndex + lazyGridState.layoutInfo.visibleItemsInfo.size >= lazyGridState.layoutInfo.totalItemsCount

    fun onExpanded() {
        _expanded.value = !_expanded.value
    }

    fun createPreference(context: Context) {
        SharedPreferenceManager.create(context)
        _isApiKey.value = isApikeyAndPrivateKeyInPreference()
    }

    private fun isApikey(): Boolean = SharedPreferenceManager.getString(KeysManger.API_KEY).isNullOrEmpty()

    private fun isPrivateKey(): Boolean = SharedPreferenceManager.getString(KeysManger.PRIVATE_KEY).isNullOrEmpty()

    private fun isApikeyAndPrivateKeyInPreference(): Boolean =  !isApikey() && !isPrivateKey()

    fun reset() {
        _isApiKey.value = false
        _page = -1
        _total = 0
        _charactersTotal = emptyList()
        _animate.value = false
        _expanded.value = false
        _isError.value = false
        _isLoading.value = false
        _filterText.value = ""
    }

}