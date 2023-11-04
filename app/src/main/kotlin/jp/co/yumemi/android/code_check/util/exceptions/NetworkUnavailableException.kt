package jp.co.yumemi.android.code_check.util.exceptions

import io.ktor.utils.io.errors.IOException

class NetworkUnavailableException(message: String) : IOException(message)