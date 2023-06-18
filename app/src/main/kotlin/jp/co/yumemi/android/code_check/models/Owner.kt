package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing the owner of a GitHub account.
 *
 * @param avatarUrl The URL of the owner's avatar image.
 */
@Parcelize
data class Owner(
    @SerializedName("avatar_url") val avatarUrl: String?
) : Parcelable
