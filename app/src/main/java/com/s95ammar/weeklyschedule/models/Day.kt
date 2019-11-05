package com.s95ammar.weeklyschedule.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Day (
        var name: String,
        @ColumnInfo(name = "schedule_id") var scheduleId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int =0




}
