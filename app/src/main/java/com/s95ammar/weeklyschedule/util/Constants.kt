package com.s95ammar.weeklyschedule.util

import androidx.annotation.IntDef
import org.joda.time.LocalTime

enum class Mode { ADD, EDIT }
const val DATABASE_NAME = "schedules_database"
const val HOURS_IN_DAY = 24
const val MINUTES_IN_HOUR = 60
val DEFAULT_TIME = LocalTime(12, 0)
const val TIME_PATTERN_12H = "hh:mm aa"
const val TIME_PATTERN_24H = "HH:mm"

