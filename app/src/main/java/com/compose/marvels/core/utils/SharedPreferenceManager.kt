package com.compose.marvels.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferenceManager {

    private const val name = "secret_shared_prefs"
    private lateinit var sharedPreferences: SharedPreferences

    fun create(context: Context) {
        val masterKey = getMasterKey(context)

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            name,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(context: Context) =
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()


    fun getString(key: String) = sharedPreferences.getString(key, "")

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)

    fun getInt(key: String) = sharedPreferences.getInt(key, -1)

    fun setString(key: String, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }
}