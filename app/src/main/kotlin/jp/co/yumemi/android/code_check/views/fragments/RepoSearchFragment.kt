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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
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
     * Sets up the UI components and observes the changes in the GitHub repository list.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoSearchLayoutManager = LinearLayoutManager(requireContext())
        val repoSearchDividerItemDecoration =
            DividerItemDecoration(requireContext(), repoSearchLayoutManager.orientation)
        adapter = GitHubRepoRecyclerViewAdapter(object :
            GitHubRepoRecyclerViewAdapter.OnItemClickListener {

            /**
             * Handles item click events.
             *
             * @param item The clicked GitHubAccount item.
             */
            override fun itemClick(item: GitHubAccount) {
                gotoRepositoryFragment(item)
            }
        })

        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(requireContext(), editText)
                    editText.text.toString().let {
                        if (it.isBlank()) {
                            showNoTermSearchDialog()
                        } else {
                            viewModel.searchRepositories(it)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also {
            it.layoutManager = repoSearchLayoutManager
            it.addItemDecoration(repoSearchDividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.gitHubRepoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            if (it) {
                binding.lytLoading.bubbleHolder.visibility = VISIBLE
            } else {
                binding.lytLoading.bubbleHolder.visibility = GONE
            }
        }

        viewModel.showError.observe(viewLifecycleOwner) {
            it?.let {
                showErrorDialog(CustomErrorMessage.createMessage(it, requireContext()))
            }
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
        dialog.show(childFragmentManager, "custom_dialog_no_term_search")

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
        dialog.show(childFragmentManager, "custom_dialog_error")

    }
}




