package jp.co.yumemi.android.code_check.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.models.GitHubSearchResponse
import jp.co.yumemi.android.code_check.models.Owner
import jp.co.yumemi.android.code_check.network.util.ApiResultState
import jp.co.yumemi.android.code_check.repository.GitHubAccountRepository
import jp.co.yumemi.android.code_check.util.exceptions.CustomErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class RepoSearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var gitHubAccountRepository: GitHubAccountRepository

    @Mock
    private lateinit var showLoaderObserver: Observer<Boolean>

    @Mock
    private lateinit var showErrorObserver: Observer<CustomErrorModel?>

    private lateinit var viewModel: RepoSearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)
        viewModel = RepoSearchViewModel(gitHubAccountRepository)
        viewModel.showLoader.observeForever(showLoaderObserver)
        viewModel.showError.observeForever(showErrorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.showLoader.removeObserver(showLoaderObserver)
        viewModel.showError.removeObserver(showErrorObserver)
    }

    @Test
    fun testSearchRepositories_Success() = runBlocking {
        val testDatalist = GitHubSearchResponse(
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

        `when`(gitHubAccountRepository.getGitHubAccountFromDataSource(anyString()))
            .thenReturn(ApiResultState.Success(testDatalist))

        viewModel.searchRepositories("kotlin")

        verify(showLoaderObserver).onChanged(true)

        verify(showLoaderObserver).onChanged(false)

        // Can assert the LiveData values like this:
        assert(viewModel.gitHubRepoList.value == testDatalist.items)
    }

    @Test
    fun testSearchRepositories_Failure() = runBlocking {
        val errorMessage = "Error message"
        `when`(gitHubAccountRepository.getGitHubAccountFromDataSource(anyString()))
            .thenReturn(ApiResultState.Failed(R.string.something_wrong, errorMessage))

        viewModel.searchRepositories("query")

        verify(showLoaderObserver).onChanged(true)
        verify(showLoaderObserver).onChanged(false)
        verify(showErrorObserver).onChanged(
            CustomErrorModel(R.string.something_wrong, errorMessage)
        )
        verify(showLoaderObserver).onChanged(false)
    }

    @Test
    fun testResetShowError() {
        viewModel.resetShowError()

        verify(showErrorObserver).onChanged(null)
    }

}