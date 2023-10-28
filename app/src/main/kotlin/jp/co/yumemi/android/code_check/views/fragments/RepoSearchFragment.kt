/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.app.AlertDialog
import android.content.Context
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

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
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
                    editText.text.toString().let {
                        if (it.isBlank()) {
                            showNoTermSearchDialog(requireContext())
                        }
                        viewModel.searchRepositories(it)

                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
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
    }

    /**
     * Navigates to the repository details fragment.
     *
     * @param item The selected GitHubAccount item.
     */
    fun gotoRepositoryFragment(item: GitHubAccount) {
        val _action =
            RepoSearchFragmentDirections.actionRepoSearchFragmentToRepoDetailsFragment(repository = item)
        findNavController().navigate(_action)
    }

    /**
     * Displays a dialog for indicating no search term provided.
     *
     * @param context The context to show the dialog.
     */
    private fun showNoTermSearchDialog(context: Context) {
        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.title_no_term_search))
            setIcon(android.R.drawable.ic_dialog_info)
            setMessage(context.getString(R.string.msg_no_term_search))
            setPositiveButton(context.getString(R.string.response_ok), null)
            show()
        }
    }
}




