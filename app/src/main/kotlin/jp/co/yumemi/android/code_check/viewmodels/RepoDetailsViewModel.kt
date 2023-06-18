package jp.co.yumemi.android.code_check.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.code_check.models.GitHubAccount

class RepoDetailsViewModel:ViewModel() {
    private var _gitHubRepoDetails = MutableLiveData<GitHubAccount>(null)
    val gitHubRepoDetails : LiveData<GitHubAccount>
        get() = _gitHubRepoDetails

    fun setRepoDetails(gitHubAccount: GitHubAccount){
        _gitHubRepoDetails.value = gitHubAccount
    }
}