package com.dev.caiovinicius.planner.presentation.ui.viewmodel

data class NewPlannerActivity(
    val name: String? = null,
    val date: SetDate? = null,
    val time: SetTime? = null
) {
    fun isFilled(): Boolean = !name.isNullOrEmpty() && date != null && time != null
}

data class SetDate(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
)

data class SetTime(
    val hourOfDay: Int,
    val minute: Int,
)
