package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubAccount(
    val name: String?,
    val owner: Owner?,
    val language: String?,
    @SerializedName("stargazers_count") val stargazersCount: Long?,
    @SerializedName("watchers_count") val watchersCount: Long?,
    @SerializedName("forks_count") val forksCount: Long?,
    @SerializedName("open_issues_count") val openIssuesCount: Long?,
) : Parcelable
