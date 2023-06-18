package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitHubAccountRepository @Inject constructor(private val gitHubAccountApiService: GitHubAccountApiService) {

    suspend fun getGitHubAccountFromDataSource(query: String): GitHubSearchResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService(query)
        }
    }

    private suspend fun getResponseFromRemoteService(query: String): GitHubSearchResponse? {
        val response = gitHubAccountApiService.getGitHubRepositories(query)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}