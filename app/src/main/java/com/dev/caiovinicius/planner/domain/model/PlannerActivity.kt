package com.dev.caiovinicius.planner.domain.model

import android.icu.util.Calendar
import com.dev.caiovinicius.planner.domain.utils.createCalendarFromTimeInMillis
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityDate
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityDateTime
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityTime

data class PlannerActivity(
    val uuid: String,
    val name: String,
    val datetime: Long,
    val isCompleted: Boolean
) {
    private val datetimeCalendar: Calendar = createCalendarFromTimeInMillis(datetime)
    val dateString: String = datetimeCalendar.toPlannerActivityDate()
    val timeString: String = datetimeCalendar.toPlannerActivityTime()
    val datetimeString: String = datetimeCalendar.toPlannerActivityDateTime()
}
