package com.example.shoppinglistnew.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistnew.di.IoDispatcher
import com.example.shoppinglistnew.domain.repositories.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _authenticationUiState = MutableLiveData<Boolean>()
    val authenticationUiState get() = _authenticationUiState

    init {
        getAuthentication()
    }

    fun getAuthentication() {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                authenticationRepository.getAuthentication(getKey()!!)
            }.onFailure {
                _authenticationUiState.postValue(false)
            }.onSuccess {
                _authenticationUiState.postValue(it.get(STRING_SUCCESS).asBoolean)
            }
        }
    }

    private suspend fun getKey(): String? {
        val preferencesKey = stringPreferencesKey(STRING_KEY)
        val result = viewModelScope.async(ioDispatcher) {
            var key = dataStore.data.map { it[preferencesKey] }.firstOrNull()
            if (key.isNullOrEmpty()) {
                val response = authenticationRepository.getKey()
                if (response.isSuccessful) {
                    response.body()?.let {
                        key = it
                        dataStore.edit { preferences ->
                            preferences[preferencesKey] = it
                        }
                    }
                }
            }
            key
        }
        return result.await()
    }

    companion object {
        private const val STRING_KEY = "key"
        private const val STRING_SUCCESS = "success"
    }
}