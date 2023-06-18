package jp.co.yumemi.android.code_check.network

import jp.co.yumemi.android.code_check.constants.Constant
import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GitHubAccountApiService {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET(Constant.ENDPOINT_REPOSITORIES)
    suspend fun getGitHubRepositories(@Query("q") query: String):Response<GitHubSearchResponse>
}