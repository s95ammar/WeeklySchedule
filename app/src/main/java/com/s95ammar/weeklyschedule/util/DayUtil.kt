package com.s95ammar.weeklyschedule.util

val DAYS_OF_WEEK = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
const val DAYS_AMOUNT_IN_A_WEEK = 7

fun getDayOfWeek(dayNumInSchedule: Int) = DAYS_OF_WEEK[dayNumInSchedule % DAYS_AMOUNT_IN_A_WEEK]
