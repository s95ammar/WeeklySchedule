package com.s95ammar.weeklyschedule.util

import org.joda.time.LocalTime

enum class ScheduleMode { VIEW, EDIT }
enum class ListMode { ADD, EDIT }
const val SHARED_PREFERENCES = "shared preferences"
const val ACTIVE_SCHEDULE_ID_KEY = "active schedule"
const val DATABASE_NAME = "schedules_database"
const val HOURS_IN_DAY = 24
const val MINUTES_IN_HOUR = 60
val DEFAULT_TIME = LocalTime(12, 0)
const val TIME_PATTERN_12H = "hh:mm aa"
const val TIME_PATTERN_24H = "HH:mm"

