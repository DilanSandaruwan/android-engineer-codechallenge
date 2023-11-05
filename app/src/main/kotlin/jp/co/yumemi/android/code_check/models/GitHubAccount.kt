package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a GitHub account or repository.
 * Implements Parcelable for efficient data transfer between components.
 *
 * @property name The name of the GitHub account or repository.
 * @property fullName The full name of the GitHub account or repository.
 * @property owner The owner of the GitHub account or repository.
 * @property htmlUrl The HTML URL of the GitHub account or repository.
 * @property description A brief description of the GitHub account or repository.
 * @property language The programming language used in the GitHub account or repository.
 * @property stargazersCount The number of stargazers (users who have marked the account or repository as a favorite).
 * @property watchersCount The number of users who are watching this account or repository.
 * @property forksCount The number of forks (duplicated repositories) of this account or repository.
 * @property openIssuesCount The number of open issues for this account or repository.
 * @property watchers The number of users watching this account or repository.
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
