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
import jp.co.yumemi.android.code_check.network.util.ApiResultState
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

    private val _showLoader = MutableLiveData<Boolean>(false)
    val showLoader: LiveData<Boolean>
        get() = _showLoader

    /**
     * LiveData representing the list of GitHub repositories.
     */
    private val _gitHubRepoList = MutableLiveData<List<GitHubAccount>>()
    val gitHubRepoList: LiveData<List<GitHubAccount>>
        get() = _gitHubRepoList

    /**
     * Search repositories based on the provided input text.
     *
     * @param inputText The search input text.
     */
    fun searchRepositories(inputText: String) {

        viewModelScope.launch {
            _showLoader.postValue(true)
            when (val response =
                gitHubAccountRepository.getGitHubAccountFromDataSource(inputText)) {
                is ApiResultState.Success -> {
                    _showLoader.postValue(false)
                    _gitHubRepoList.value = response.data?.items ?: emptyList()
                }

                is ApiResultState.Failed -> {
                    _showLoader.postValue(false)
                    _gitHubRepoList.value = emptyList()
                }
            }
        }
    }

}

