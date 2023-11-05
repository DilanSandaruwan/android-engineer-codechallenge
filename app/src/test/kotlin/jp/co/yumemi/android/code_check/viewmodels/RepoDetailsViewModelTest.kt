package jp.co.yumemi.android.code_check.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.models.Owner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

internal class RepoDetailsViewModelTest {
    // Use this rule to make LiveData work with JUnit
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // The ViewModel under test
    private lateinit var viewModel: RepoDetailsViewModel

    @Mock
    private lateinit var gitHubAccountObserver: Observer<GitHubAccount>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RepoDetailsViewModel()
        viewModel.gitHubRepoDetails.observeForever(gitHubAccountObserver)
    }

    @Test
    fun testSetRepoDetails() {
        // Create a sample GitHubAccount
        val gitHubAccount = GitHubAccount(
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

        // Call the ViewModel's setRepoDetails method
        viewModel.setRepoDetails(gitHubAccount)

        // Verify that the LiveData is updated with the expected data
        Mockito.verify(gitHubAccountObserver).onChanged(gitHubAccount)
    }
}