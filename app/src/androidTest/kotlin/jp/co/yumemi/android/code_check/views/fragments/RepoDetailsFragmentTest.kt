package jp.co.yumemi.android.code_check.views.fragments

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.GitHubAccount
import jp.co.yumemi.android.code_check.models.Owner
import jp.co.yumemi.android.code_check.views.activities.TopActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class RepoDetailsFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TopActivity::class.java)

    @Before
    fun setUp() {

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

        // Perform the fragment transaction in onCreate or onCreateView
        activityRule.scenario.onActivity { activity ->
            val fragment = RepoDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable("repository", gitHubAccount)
            fragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }
    }

    @Test
    fun testRepoDetailsFragmentUI() {
        // Wait for the fragment to be fully loaded and displayed
        onView(withId(R.id.ownerIconView)).check(matches(isDisplayed()))

        // Perform interactions and assertions as needed
        onView(withId(R.id.navFloatingActionButton)).perform(click())

    }
}