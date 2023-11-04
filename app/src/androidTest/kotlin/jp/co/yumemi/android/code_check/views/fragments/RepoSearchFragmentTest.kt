package jp.co.yumemi.android.code_check.views.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.views.activities.TopActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

internal class RepoSearchFragmentTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var activityScenarioRule =
        ActivityScenarioRule(TopActivity::class.java)// Launch the fragment with the mocked ViewModel

    @get:Rule
    var activityTestRule = ActivityTestRule(TopActivity::class.java)

    @Before
    fun setup() {
        // Initialize any necessary setup or mocks here
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test_search_input_text_and_load_into_recyclerView`() {
        // Launch the Activity containing the RepoSearchFragment
        val activityScenario = ActivityScenario.launch(TopActivity::class.java)

        // Type a search query and click the search button
        onView(withId(R.id.searchInputText)).perform(typeText("android"))

        // Press the Enter key on the soft input keyboard
        onView(withId(R.id.searchInputText)).perform(pressImeActionButton())

        // Verify that the loading view is displayed
        onView(withId(R.id.lytLoading)).check(matches(isDisplayed()))

        // Wait for the data to load (may need to customize this part)
        // Can use IdlingResource or a similar mechanism to wait for data to load
        Thread.sleep(2000)

        // Verify that the RecyclerView displays data
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun testSearchRepositories() {
        // Type a search term into the search input
        onView(withId(R.id.searchInputText)).perform(typeText("android"))

        // Press the search button
        onView(withId(R.id.searchInputText)).perform(pressImeActionButton())

        // Verify that the loading indicator is displayed
        onView(withId(R.id.lytLoading)).check(matches(isDisplayed()))

        // Wait for data to load (may need to use IdlingResource or other techniques)
        Thread.sleep(10000)

        // Verify that the loading indicator is gone
        onView(withId(R.id.lytLoading)).check(matches(not(isDisplayed())))

        // Verify that the RecyclerView has items
        onView(withId(R.id.recyclerView)).check(matches(hasMinimumChildCount(1)))

    }

}
