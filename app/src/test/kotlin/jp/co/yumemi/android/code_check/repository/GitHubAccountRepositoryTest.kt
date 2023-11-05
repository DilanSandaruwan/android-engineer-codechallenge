package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.models.Owner
import jp.co.yumemi.android.code_check.network.GitHubAccountApiService
import jp.co.yumemi.android.code_check.network.util.ApiResultState
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
internal class GitHubAccountRepositoryTest{
    @Mock
    private lateinit var gitHubAccountApiService: GitHubAccountApiService

    private lateinit var repository: GitHubAccountRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = GitHubAccountRepository(gitHubAccountApiService)
    }

    @Test
    fun testGetGitHubAccountFromDataSource_Success() = runBlocking {
        val testDataList = GitHubSearchResponse(
            total_count = 283514,
            incomplete_results = false,
            items = arrayListOf(
                GitHubAccount(
                    name = "kotlin",
                    fullName = "JetBrains/kotlin",
                    owner = Owner(
                        login = "JetBrains",
                        avatarUrl = "https://avatars.githubusercontent.com/u/878437?v=4",
                    ),
                    htmlUrl = "https://github.com/JetBrains/kotlin",
                    description = "The Kotlin Programming Language. ",
                    stargazersCount = 46145,
                    watchersCount = 46145,
                    language = "Kotlin",
                    forksCount = 5698,
                    openIssuesCount = 162,
                    watchers = 46145,
                )
            )
        )

        `when`(gitHubAccountApiService.getGitHubRepositories(anyString()))
            .thenReturn(Response.success(testDataList))

        val result = repository.getGitHubAccountFromDataSource("kotlin")

        verify(gitHubAccountApiService).getGitHubRepositories("kotlin")
        assert(result is ApiResultState.Success)
        assert((result as ApiResultState.Success).data == testDataList)
    }
}