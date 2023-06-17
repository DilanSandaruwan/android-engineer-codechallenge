package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GitHubSearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GitHubAccount>
)
