package com.streamwide.haythemmejerbi.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamwide.haythemmejerbi.R
import com.streamwide.haythemmejerbi.domain.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val currentState: SearchContract.SearchViewState
        get() = viewState.value

    val viewState: StateFlow<SearchContract.SearchViewState> get() = _viewState
    private val _viewState = MutableStateFlow(SearchContract.SearchViewState())

    private val _viewEffect: Channel<SearchContract.SearchViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    private var job: Job = Job()

    /**
     * Handle events
     */
    fun setEvent(event: SearchContract.SearchEvent) {
        when (event) {
            SearchContract.SearchEvent.OnInitialization -> {
                observeContactsUpdates()
                fetchContacts()
            }
            is SearchContract.SearchEvent.OnSearchQueryUpdated -> searchContactsBy(query = event.query)
        }
    }

    /**
     * Set new ui state
     */
    private fun setState(reduce: SearchContract.SearchViewState.() -> SearchContract.SearchViewState) {
        val newState = currentState.reduce()
        _viewState.update { newState }
    }

    /**
     * Set new effect
     */
    private fun setEffect(builder: () -> SearchContract.SearchViewEffect) {
        val effectValue = builder()
        setState { copy(loading = false) }
        viewModelScope.launch { _viewEffect.send(effectValue) }
    }

    private fun observeContactsUpdates() {
        viewModelScope.launch {
            contactsRepository.observeChanges().collectLatest {
                setEffect { SearchContract.SearchViewEffect.ShowToast(message = R.string.synchronisation) }
                fetchContacts()
            }
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            contactsRepository.fetchContacts().collectLatest { contacts ->
                setState {
                    copy(
                        loading = false,
                        contacts = contacts,
                        noContactsMessageVisibility = contacts.isEmpty()
                    )
                }
            }
        }
    }

    private fun searchContactsBy(query: String) {
        job.cancel()
        job = viewModelScope.launch {
            setState { copy(loading = true) }
            contactsRepository.searchBy(query = query).collectLatest { contacts ->
                setState {
                    copy(
                        loading = false,
                        contacts = contacts,
                        noContactsMessageVisibility = contacts.isEmpty()
                    )
                }
            }
        }
    }
}
