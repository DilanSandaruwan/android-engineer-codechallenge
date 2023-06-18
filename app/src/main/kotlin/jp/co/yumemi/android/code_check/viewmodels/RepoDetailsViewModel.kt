package jp.co.yumemi.android.code_check.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.code_check.models.GitHubAccount

/**
 * ViewModel class for managing repository details.
 */
class RepoDetailsViewModel : ViewModel() {
    private var _gitHubRepoDetails = MutableLiveData<GitHubAccount>(null)

    /**
     * LiveData representing the GitHub account details.
     */
    val gitHubRepoDetails: LiveData<GitHubAccount>
        get() = _gitHubRepoDetails

    /**
     * Sets the GitHub account details.
     *
     * @param gitHubAccount The GitHubAccount object representing the account details.
     */
    fun setRepoDetails(gitHubAccount: GitHubAccount) {
        _gitHubRepoDetails.value = gitHubAccount
    }
}