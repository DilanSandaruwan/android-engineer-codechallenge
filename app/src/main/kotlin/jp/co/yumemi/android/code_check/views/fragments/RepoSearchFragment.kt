/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepoSearchBinding
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.viewmodels.OneViewModel
import jp.co.yumemi.android.code_check.views.adapters.GitHubRepoRecyclerViewAdapter

@AndroidEntryPoint
class RepoSearchFragment : Fragment(R.layout.fragment_repo_search) {

    lateinit var viewModel: OneViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _binding = FragmentRepoSearchBinding.bind(view)

        viewModel = ViewModelProvider(this)[OneViewModel::class.java]

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
                        viewModel.searchResults(it)
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

        viewModel.gitHubRepoList.observe(viewLifecycleOwner){
            _adapter.submitList(it)
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


