package com.example.intexsoft.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intexsoft.presentation.model.DVOItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _itemsList = MutableStateFlow(
        listOf(
            DVOItem(title = "a", body = "aa"),
            DVOItem().copy(title = "bb", body = "bb"),
            DVOItem().copy(title = "c", body = "cc"),
            DVOItem().copy(title = "d", body = "dd"),
            DVOItem().copy(title = "e", body = "ee"),
        )
    )


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    val itemsList = searchText
        .combine(_itemsList) { text, items ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                items
            }
            items.filter { item ->// filter and return a list of countries based on the text the user typed
                item.title.uppercase().contains(text.trim().uppercase()) ||
                        item.body.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _itemsList.value
        )
}
