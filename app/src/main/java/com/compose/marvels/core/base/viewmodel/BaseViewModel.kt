package com.compose.marvels.core.base.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.compose.marvels.core.base.models.APIKey
import com.compose.marvels.core.utils.KeysManger
import com.compose.marvels.core.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

open class BaseViewModel: ViewModel() {

    private val _apiKey = MutableStateFlow(APIKey())
    val apiKey: StateFlow<APIKey> = _apiKey

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private fun isValidApiKey(): Boolean = _apiKey.value.apiKey.isNotEmpty() && _apiKey.value.apiKey.length > 20

    private fun isValidPrivateKey(): Boolean = _apiKey.value.privateKey.isNotEmpty() && _apiKey.value.privateKey.length > 30

    private fun isApikey(): Boolean = SharedPreferenceManager.getString(KeysManger.API_KEY).isNullOrEmpty()

    private fun isPrivateKey(): Boolean = SharedPreferenceManager.getString(KeysManger.PRIVATE_KEY).isNullOrEmpty()

    private fun isApikeyAndPrivateKeyInPreference(): Boolean =  !isApikey() && !isPrivateKey()

    fun setLoading(value: Boolean) { _isLoading.update { value } }

    fun createPreference(context: Context) {
        SharedPreferenceManager.create(context)
        _apiKey.update {
            it.copy(
                isApiKey = isApikeyAndPrivateKeyInPreference(),
                apiKey = SharedPreferenceManager.getString(KeysManger.API_KEY) ?: "",
                privateKey = SharedPreferenceManager.getString(KeysManger.PRIVATE_KEY) ?: "",
                enabled = isValidApiKey() && isValidPrivateKey()
            )
        }
    }

    fun onSaveClick() {
        SharedPreferenceManager.setString(KeysManger.API_KEY, _apiKey.value.apiKey)
        SharedPreferenceManager.setString(KeysManger.PRIVATE_KEY, _apiKey.value.privateKey)
        _apiKey.update { it.copy(isApiKey = isApikeyAndPrivateKeyInPreference()) }
    }

    fun onApikeyChange(apikey: String) {
        _apiKey.update { it.copy(apiKey = apikey, enabled = isValidApiKey() && isValidPrivateKey()) }
    }

    fun onPrivateKeyChange(privateKey: String) {
        _apiKey.update { it.copy(privateKey = privateKey, enabled = isValidApiKey() && isValidPrivateKey()) }
    }
}