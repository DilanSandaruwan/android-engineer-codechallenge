package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import jp.co.yumemi.android.code_check.network.util.ApiResultState
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
    suspend fun getGitHubAccountFromDataSource(query: String): ApiResultState<GitHubSearchResponse?> {
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
    private suspend fun getResponseFromRemoteService(query: String): ApiResultState<GitHubSearchResponse?> {
        return try {
            val response = gitHubAccountApiService.getGitHubRepositories(query)
            if (response.isSuccessful) {
                ApiResultState.Success(response.body())
            } else {
                ApiResultState.Failed("Something went wrong!")
            }
        } catch (exception: Exception) {
            ApiResultState.Failed("Something went wrong!")
        }
    }
}