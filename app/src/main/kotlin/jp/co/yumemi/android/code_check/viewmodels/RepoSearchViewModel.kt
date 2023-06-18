/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.repository.GitHubAccountRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing repository search.
 */
@HiltViewModel
class RepoSearchViewModel @Inject constructor(
    private val gitHubAccountRepository: GitHubAccountRepository
) : ViewModel() {

    /**
     * LiveData representing the list of GitHub repositories.
     */
    private val _gitHubRepoList = MutableLiveData<List<GitHubAccount>>()
    val gitHubRepoList: LiveData<List<GitHubAccount>>
        get() = _gitHubRepoList

    /**
     * Fixme :
     * Bug : "Double tap the search icon => Empty List appears"
     * TODO: Investigate and fix the bug where double-tapping the search icon results in an empty list.
     */

    /**
     * Search repositories based on the provided input text.
     *
     * @param inputText The search input text.
     */
    fun searchRepositories(inputText: String) {
        viewModelScope.launch {
            _gitHubRepoList.value =
                gitHubAccountRepository.getGitHubAccountFromDataSource(inputText)?.items
                    ?: emptyList()
        }
    }

}

