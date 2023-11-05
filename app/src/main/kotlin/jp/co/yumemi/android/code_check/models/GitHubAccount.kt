package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a GitHub account.
 *
 * @param name The name of the account.
 * @param owner The owner of the account.
 * @param language The programming language used in the account.
 * @param stargazersCount The number of stargazers for the account.
 * @param watchersCount The number of watchers for the account.
 * @param forksCount The number of forks for the account.
 * @param openIssuesCount The number of open issues for the account.
 */
@Parcelize
data class GitHubAccount(
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("stargazers_count") val stargazersCount: Long?,
    @SerializedName("watchers_count") val watchersCount: Long?,
    @SerializedName("forks_count") val forksCount: Long?,
    @SerializedName("open_issues_count") val openIssuesCount: Long?,
    @SerializedName("watchers") val watchers: Long?,
) : Parcelable
