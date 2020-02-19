package com.s95ammar.weeklyschedule.util

enum class ScheduleMode { VIEW, EDIT, NOT_DISPLAYED }
enum class Mode { ADD, EDIT }
const val SHARED_PREFERENCES = "shared_preferences"
const val ACTIVE_SCHEDULE_ID_KEY = "active_schedule"
const val DATABASE_NAME = "schedules_database"

const val KEY_MODE = "mode"
const val KEY_DAYS_INDICES = "days_indices"
const val KEY_START_TIME = "start_time"
const val KEY_END_TIME = "end_time"

val Any.LOG_TAG
	get() = "log_${this.javaClass.simpleName}"
