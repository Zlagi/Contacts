package com.streamwide.haythemmejerbi.presentation.search.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streamwide.haythemmejerbi.R
import com.streamwide.haythemmejerbi.databinding.FragmentSearchBinding
import com.streamwide.haythemmejerbi.presentation.search.view.adapter.ContactsAdapter
import com.streamwide.haythemmejerbi.presentation.search.viewmodel.SearchContract
import com.streamwide.haythemmejerbi.presentation.search.viewmodel.SearchViewModel
import com.streamwide.haythemmejerbi.presentation.utils.buildToast
import com.streamwide.haythemmejerbi.presentation.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModels<SearchViewModel>()
    private var contactsAdapter: ContactsAdapter? = null

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissions()
        createContactsAdapter()
        setupContactsRecyclerView()
        setupViews()
        observeViewState()
        observeViewEffect()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(/* input = */ Manifest.permission.READ_CONTACTS)
        } else {
            searchViewModel.setEvent(event = SearchContract.SearchEvent.OnInitialization)
        }
    }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        when (it) {
            true -> {
                searchViewModel.setEvent(SearchContract.SearchEvent.OnInitialization)
            }
            false -> {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createContactsAdapter() {
        contactsAdapter = ContactsAdapter()
    }

    private fun setupContactsRecyclerView() {
        binding.recyclerView.apply {
            adapter = contactsAdapter
        }
    }

    private fun setupViews() {
        binding.etSearchInput.addTextChangedListener(afterTextChanged = {
            val query = it?.toString() ?: ""
            searchViewModel.setEvent(SearchContract.SearchEvent.OnSearchQueryUpdated(query = query))
        })
    }

    private fun observeViewState() {
        viewLifecycleOwner.collectWhenStarted(searchViewModel.viewState) {
            render(state = it)
        }
    }

    private fun render(state: SearchContract.SearchViewState) {
        binding.apply {
            progressBar.isVisible = state.loading
            recyclerView.isVisible = !state.noContactsMessageVisibility
            noResultsTextView.isVisible = state.noContactsMessageVisibility
            contactsAdapter?.submitList(state.contacts)
        }
    }

    private fun observeViewEffect() {
        viewLifecycleOwner.collectWhenStarted(searchViewModel.viewEffect) {
            reactTo(it)
        }
    }

    private fun reactTo(effect: SearchContract.SearchViewEffect) {
        when (effect) {
            is SearchContract.SearchViewEffect.ShowToast -> {
                toast = requireContext().buildToast(resources.getString(/* id = */ effect.message))
                toast?.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toast = null
        _binding = null
    }
}
