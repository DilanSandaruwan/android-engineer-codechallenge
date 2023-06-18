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
    val forksCount: Long?,
    val openIssuesCount: Long?,
):Parcelable

//@Parcelize
//data class GitHubAccount(
//    val name: String,
//    val ownerIconUrl: String,
//    val language: String,
//    val stargazersCount: Long,
//    val watchersCount: Long,
//    val forksCount: Long,
//    val openIssuesCount: Long,
//) : Parcelable
