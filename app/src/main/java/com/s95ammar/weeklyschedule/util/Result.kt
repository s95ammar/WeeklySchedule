package com.s95ammar.weeklyschedule.util

import androidx.annotation.StringRes

sealed class Result {
	class Success: Result()
	class Error(@StringRes var message: Int): Result()
}