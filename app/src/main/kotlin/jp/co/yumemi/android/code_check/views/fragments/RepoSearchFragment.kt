/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepoSearchBinding
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.viewmodels.OneViewModel
import jp.co.yumemi.android.code_check.views.adapters.GitHubRepoRecyclerViewAdapter

class RepoSearchFragment : Fragment(R.layout.fragment_repo_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _binding = FragmentRepoSearchBinding.bind(view)

        val _viewModel = OneViewModel(requireContext())

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        val _adapter = GitHubRepoRecyclerViewAdapter(object : GitHubRepoRecyclerViewAdapter.OnItemClickListener {
            override fun itemClick(item: GitHubAccount) {
                gotoRepositoryFragment(item)
            }
        })

        _binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        _viewModel.searchResults(it).apply {
                            _adapter.submitList(this)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        _binding.recyclerView.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = _adapter
        }
    }

    fun gotoRepositoryFragment(item: GitHubAccount) {
        val _action =
            RepoSearchFragmentDirections.actionRepoSearchFragmentToRepoDetailsFragment(item = item)
        findNavController().navigate(_action)
    }
}

val diff_util = object : DiffUtil.ItemCallback<GitHubAccount>() {
    override fun areItemsTheSame(oldItem: GitHubAccount, newItem: GitHubAccount): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GitHubAccount, newItem: GitHubAccount): Boolean {
        return oldItem == newItem
    }

}


