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
 * RepoDetailsFragment で使う
 */
@HiltViewModel
class OneViewModel @Inject constructor(
    private val gitHubAccountRepository: GitHubAccountRepository
) : ViewModel() {

    private val _gitHubRepoList = MutableLiveData<List<GitHubAccount>>()
    val gitHubRepoList: LiveData<List<GitHubAccount>>
        get() = _gitHubRepoList

    /**
     * Fixme :
     * Bug : "Double tap the search icon => Empty List appears"
     */

    fun searchRepositories(inputText: String) {
        viewModelScope.launch {
            _gitHubRepoList.value =
                gitHubAccountRepository.getGitHubAccountFromDataSource(inputText)?.items ?: emptyList()
        }
    }

}

