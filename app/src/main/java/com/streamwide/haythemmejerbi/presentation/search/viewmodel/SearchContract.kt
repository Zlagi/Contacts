package com.streamwide.haythemmejerbi.presentation.search.viewmodel

import androidx.annotation.StringRes
import com.streamwide.haythemmejerbi.data.model.Contact

class SearchContract {

    sealed class SearchEvent {
        object OnInitialization : SearchEvent()
        data class OnSearchQueryUpdated(val query: String) : SearchEvent()
    }

    sealed class SearchViewEffect {
        data class ShowToast(@StringRes val message: Int) : SearchViewEffect()
    }

    data class SearchViewState(
        val loading: Boolean = false,
        val contacts: List<Contact> = listOf(),
        val noContactsMessageVisibility: Boolean = false,
    )
}
