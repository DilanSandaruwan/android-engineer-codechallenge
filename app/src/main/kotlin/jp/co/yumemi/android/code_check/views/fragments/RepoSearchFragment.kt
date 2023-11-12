/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.TagConstant
import jp.co.yumemi.android.code_check.databinding.FragmentRepoSearchBinding
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.util.components.CustomDialogFragment
import jp.co.yumemi.android.code_check.util.components.KeyBoardUtil
import jp.co.yumemi.android.code_check.util.exceptions.CustomErrorMessage
import jp.co.yumemi.android.code_check.viewmodels.RepoSearchViewModel
import jp.co.yumemi.android.code_check.views.adapters.GitHubRepoRecyclerViewAdapter

/**
 * Fragment for searching GitHub repositories.
 */
@AndroidEntryPoint
class RepoSearchFragment : Fragment() {

    private lateinit var binding: FragmentRepoSearchBinding
    private lateinit var adapter: GitHubRepoRecyclerViewAdapter

    /**
     * Initializes the ViewModel.
     */
    private val viewModel: RepoSearchViewModel by viewModels()

    /**
     * Inflates the layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentRepoSearchBinding.inflate(layoutInflater, container, false).apply {
                searchVM = viewModel
                lifecycleOwner = viewLifecycleOwner
            }

        return binding.root
    }

    /**
     * Sets up the UI components and observes changes in the GitHub repository list.
     * Initializes the RecyclerView, sets up the search input action listener,
     * and observes changes in the ViewModel to update the UI accordingly.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize and configure the RecyclerView for displaying GitHub repositories.
        setupRecyclerView()

        // Set up an action listener for the search input to trigger a search when the "Search" action is performed.
        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearchAction(editText as EditText)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        // Observe changes in the ViewModel to update the UI based on LiveData events.
        observeViewModel()
    }

    /**
     * Observes changes in the ViewModel's LiveData and updates the UI accordingly.
     * Observes the GitHub repository list, loading indicator visibility, and error events.
     */
    private fun observeViewModel() {
        // Observe changes in the GitHub repository list and update the RecyclerView adapter.
        viewModel.gitHubRepoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Observe changes in the loading indicator visibility and update the UI accordingly.
        viewModel.showLoader.observe(viewLifecycleOwner) {
            binding.lytLoading.bubbleHolder.visibility = if (it) VISIBLE else GONE
        }

        // Observe errors and display an error dialog if an error is present.
        viewModel.showError.observe(viewLifecycleOwner) {
            it?.let {
                showErrorDialog(CustomErrorMessage.createMessage(it, requireContext()))
            }
        }
    }

    /**
     * Sets up the RecyclerView for displaying GitHub repositories.
     * Configures the layout manager, item decoration, and assigns an adapter
     * with a callback for handling item clicks.
     */
    private fun setupRecyclerView() {
        // Create a LinearLayoutManager to manage the layout of items in the RecyclerView.
        val repoSearchLayoutManager = LinearLayoutManager(requireContext())

        // Add a divider between RecyclerView items for improved visual separation.
        val repoSearchDividerItemDecoration = DividerItemDecoration(requireContext(), repoSearchLayoutManager.orientation)

        // Initialize the RecyclerView adapter with an item click callback.
        adapter = GitHubRepoRecyclerViewAdapter(object : GitHubRepoRecyclerViewAdapter.OnItemClickListener {
            /**
             * Handles item click events.
             *
             * @param item The clicked GitHubAccount item.
             */
            override fun itemClick(item: GitHubAccount) {
                gotoRepositoryFragment(item)
            }
        })

        with(binding.recyclerView) {
            this.layoutManager = repoSearchLayoutManager
            addItemDecoration(repoSearchDividerItemDecoration)
            adapter = this@RepoSearchFragment.adapter
        }
    }

    /**
     * Navigates to the repository details fragment.
     *
     * @param item The selected GitHubAccount item.
     */
    fun gotoRepositoryFragment(item: GitHubAccount) {
        val repoSearchNavDirections =
            RepoSearchFragmentDirections.actionRepoSearchFragmentToRepoDetailsFragment(repository = item)
        findNavController().navigate(repoSearchNavDirections)
    }

    /**
     * Handles the search action triggered by the user.
     *
     * This function hides the keyboard, retrieves the text from the provided EditText,
     * and then decides whether to show a dialog for an empty search term or to
     * initiate a repository search using the ViewModel.
     *
     * @param editText The EditText widget containing the search term.
     */
    private fun handleSearchAction(editText: EditText) {
        // Hide the keyboard to provide a smoother user experience.
        KeyBoardUtil.hideKeyboard(requireContext(), editText)

        // Extract the search term from the EditText.
        editText.text.toString().let {
            // Check if the search term is blank.
            if (it.isBlank()) {
                // Display a dialog to inform the user that the search term is empty.
                showNoTermSearchDialog()
            } else {
                // Initiate a search for repositories using the ViewModel.
                viewModel.searchRepositories(it)
            }
        }
    }

    /**
     * Displays a dialog for indicating no search term provided.
     *
     * @param context The context to show the dialog.
     */
    private fun showNoTermSearchDialog() {
        val dialog = CustomDialogFragment.newInstance(
            title = getString(R.string.title_no_term_search),
            message = getString(R.string.msg_no_term_search),
            positiveText = getString(R.string.response_ok),
            negativeText = "",
            positiveClickListener = {
                adapter.submitList(emptyList())
            },
            negativeClickListener = { },
            iconResId = R.drawable.ic_dialog_info
        )
        dialog.show(childFragmentManager, TagConstant.NO_TERM_SEARCH_DIALOG_TAG)

    }

    /**
     * Displays a custom error dialog with the provided error message.
     *
     * @param errMsg The error message to be displayed in the dialog.
     */
    private fun showErrorDialog(errMsg: String) {
        val dialog = CustomDialogFragment.newInstance(
            title = getString(R.string.error_title),
            message = errMsg,
            positiveText = getString(R.string.response_ok),
            negativeText = "",
            positiveClickListener = {
                viewModel.resetShowError()
            },
            negativeClickListener = { },
            iconResId = R.drawable.ic_dialog_error
        )
        dialog.show(childFragmentManager, TagConstant.ERROR_DIALOG_TAG)

    }
}




