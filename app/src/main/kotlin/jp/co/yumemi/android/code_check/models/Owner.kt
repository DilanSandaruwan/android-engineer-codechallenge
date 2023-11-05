package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing the owner of a GitHub account or repository.
 * Implements Parcelable for efficient data transfer between components.
 *
 * @property avatarUrl The URL of the owner's avatar image.
 * @property login The username or login name of the owner.
 */
@Parcelize
data class Owner(
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("login") val login: String?,
) : Parcelable
