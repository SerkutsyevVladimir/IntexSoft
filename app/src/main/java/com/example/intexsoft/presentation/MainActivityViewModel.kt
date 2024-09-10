package com.example.intexsoft.presentation

import androidx.lifecycle.ViewModel
import com.example.intexsoft.presentation.model.DVOItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _itemsList = MutableStateFlow(
        listOf(
            DVOItem(),
            DVOItem().copy(),
            DVOItem().copy(),
            DVOItem().copy(),
            DVOItem().copy(),
        )
    )
    val itemsList = _itemsList.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }
}
