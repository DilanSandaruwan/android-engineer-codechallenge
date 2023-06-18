package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class for interacting with the GitHub Account API.
 *
 * @param gitHubAccountApiService The API service for GitHub Account.
 */
class GitHubAccountRepository @Inject constructor(private val gitHubAccountApiService: GitHubAccountApiService) {

    /**
     * Retrieves GitHub account information from the data source.
     *
     * @param query The search query.
     * @return The GitHubSearchResponse object containing the account information, or null if unsuccessful.
     */
    suspend fun getGitHubAccountFromDataSource(query: String): GitHubSearchResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService(query)
        }
    }

    /**
     * Retrieves the response from the remote GitHub Account API service.
     *
     * @param query The search query.
     * @return The GitHubSearchResponse object containing the response body, or null if the response is unsuccessful.
     */
    private suspend fun getResponseFromRemoteService(query: String): GitHubSearchResponse? {
        val response = gitHubAccountApiService.getGitHubRepositories(query)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}