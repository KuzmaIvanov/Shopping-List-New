package com.example.shoppinglistnew.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistnew.di.IoDispatcher
import com.example.shoppinglistnew.domain.models.ShopListItem
import com.example.shoppinglistnew.domain.repositories.ShopListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditShopListViewModel @Inject constructor(
    private val shopListRepository: ShopListRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _shopListUiState = MutableStateFlow<Result<List<ShopListItem>>?>(null)
    val shopListUiState get() = _shopListUiState

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EditShopListViewModel", "CoroutineExceptionHandler got $exception")
    }

    fun getShopList(listId: Int, refreshIntervalMs: Long = DEFAULT_REFRESH_INTERVAL_MS) {
        viewModelScope.launch(ioDispatcher) {
            shopListRepository.getShopList(listId, refreshIntervalMs)
                .catch {
                    _shopListUiState.value = Result.failure(it)
                }
                .collect {
                    _shopListUiState.value = Result.success(it)
                }
        }
    }

    fun addToShopList(listId: Int, name: String, quantity: Int) {
        viewModelScope.launch(ioDispatcher + handler) {
            shopListRepository.addToShopList(listId, name, quantity)
        }
    }

    companion object {
        private const val DEFAULT_REFRESH_INTERVAL_MS = 2000L
    }
}