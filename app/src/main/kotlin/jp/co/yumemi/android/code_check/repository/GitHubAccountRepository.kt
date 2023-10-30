package jp.co.yumemi.android.code_check.repository

import android.util.Log
import io.ktor.utils.io.errors.IOException
import jp.co.yumemi.android.code_check.constants.Constant.TAG
import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import jp.co.yumemi.android.code_check.network.util.ApiResultState
import jp.co.yumemi.android.code_check.network.util.retryIOOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
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
            return@withContext retryIOOperation(3) {
                getResponseFromRemoteService(query)
            }
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
                when (response.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        val errorMessage = "Resource not found"
                        logError(errorMessage)
                        ApiResultState.Failed(errorMessage)
                    }

                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        val errorMessage = "Unauthorized access"
                        logError(errorMessage)
                        ApiResultState.Failed(errorMessage)
                    }

                    else -> {
                        val errorMessage = if (!response.raw().message().isNullOrEmpty()) {
                            response.raw().message()
                        } else {
                            "Something went wrong!\n- Status Code: ${response.code()}"
                        }
                        logError(errorMessage)
                        ApiResultState.Failed(errorMessage)
                    }
                }
            }
        } catch (exception: IOException) {
            val errorMessage = "Network error - ${exception.message}"
            logError(errorMessage)
            ApiResultState.Failed("Network error: ${exception.message}")
        } catch (exception: Exception) {
            val errorMessage = "Something went wrong!\n- ${exception.localizedMessage}"
            logError(errorMessage)
            ApiResultState.Failed(errorMessage)
        }
    }

    private fun logError(errorMessage: String) {
        val logTag = "$TAG - Response Check"
        Log.e(logTag, "getResponseFromRemoteService: $errorMessage")
    }
}