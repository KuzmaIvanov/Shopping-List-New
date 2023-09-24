package com.example.shoppinglistnew.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistnew.di.IoDispatcher
import com.example.shoppinglistnew.domain.models.ShopList
import com.example.shoppinglistnew.domain.repositories.ShopListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyShopListsViewModel @Inject constructor(
    private val shopListRepository: ShopListRepository,
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _shopListsUiState = MutableLiveData<Result<List<ShopList>>>()
    val shopListsUiState get() = _shopListsUiState

    private val _createdListUiState = MutableLiveData<Result<Int>>()
    val createdListUiState get() = _createdListUiState

    private val _removedListUiState = MutableLiveData<Boolean>()
    val removedListUiState get() = _removedListUiState

    init {
        getAllMyShopLists()
    }

    fun createShoppingList(name: String) {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                val preferencesKey = stringPreferencesKey(STRING_KEY)
                val key = dataStore.data.map { it[preferencesKey] }.firstOrNull()
                shopListRepository.createShopList(key!!, name)
            }.onFailure {
                _createdListUiState.postValue(Result.failure(it))
            }.onSuccess {
                _createdListUiState.postValue(Result.success(it.get(STRING_ID).asInt))
            }
        }
    }

    fun removeShoppingList(listId: Int) {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                shopListRepository.removeShopList(listId)
            }.onFailure {
                _removedListUiState.postValue(false)
            }.onSuccess {
                _removedListUiState.postValue(true)
            }
        }
    }

    fun getAllMyShopLists() {
        viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                val preferencesKey = stringPreferencesKey(STRING_KEY)
                val key = dataStore.data.map { it[preferencesKey] }.firstOrNull()
                shopListRepository.getAllMyShopLists(key!!)
            }.onFailure {
                _shopListsUiState.postValue(Result.failure(it))
            }.onSuccess {
                _shopListsUiState.postValue(Result.success(it))
            }
        }
    }

    companion object {
        private const val STRING_KEY = "key"
        private const val STRING_ID = "list_id"
    }
}