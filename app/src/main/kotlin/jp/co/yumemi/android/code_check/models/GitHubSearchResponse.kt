package jp.co.yumemi.android.code_check.models

/**
 * Data class representing the response from a GitHub search.
 *
 * @param totalCount The total count of search results.
 * @param incompleteResults Indicates if the results are incomplete or not.
 * @param items The list of GitHub accounts matching the search criteria.
 */
data class GitHubSearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GitHubAccount>
)
