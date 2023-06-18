package jp.co.yumemi.android.code_check.network

import jp.co.yumemi.android.code_check.constants.Constant
import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface for defining the GitHub Account API service.
 */
interface GitHubAccountApiService {

    /**
     * Retrieves GitHub repositories based on the provided query.
     *
     * @param query The search query.
     * @return A Response object containing the search response.
     */
    @Headers("Accept: application/vnd.github.v3+json")
    @GET(Constant.ENDPOINT_REPOSITORIES)
    suspend fun getGitHubRepositories(@Query("q") query: String): Response<GitHubSearchResponse>
}