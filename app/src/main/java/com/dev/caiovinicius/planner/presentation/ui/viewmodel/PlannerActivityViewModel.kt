package com.dev.caiovinicius.planner.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.caiovinicius.planner.core.di.MainServiceLocator
import com.dev.caiovinicius.planner.core.di.MainServiceLocator.ioDispatcher
import com.dev.caiovinicius.planner.core.di.MainServiceLocator.mainDispatcher
import com.dev.caiovinicius.planner.data.datasource.PlannerActivityLocalDataSource
import com.dev.caiovinicius.planner.domain.model.PlannerActivity
import com.dev.caiovinicius.planner.domain.utils.createCalendarFromTimeInMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

class PlannerActivityViewModel : ViewModel() {

    val plannerActivityLocalDataSource: PlannerActivityLocalDataSource by lazy {
        MainServiceLocator.plannerActivityLocalDataSource
    }

    private val _activities: MutableStateFlow<List<PlannerActivity>> = MutableStateFlow(emptyList())
    val activities: StateFlow<List<PlannerActivity>> = _activities.asStateFlow()

    private val newActivity: MutableStateFlow<NewPlannerActivity> =
        MutableStateFlow(NewPlannerActivity())

    private val updatedActivity: MutableStateFlow<PlannerActivity?> =
        MutableStateFlow(null)

    fun setSelectedActivity(selectedActivity: PlannerActivity) {
        updatedActivity.value = selectedActivity
    }

    fun clearSelectedActivity() {
        updatedActivity.value = null
    }

    fun updateSelectedActivity(
        name: String? = null,
        date: SetDate? = null,
        time: SetTime? = null
    ) {
        if (name == null && date == null && time == null) return

        updatedActivity.update { currentUpdatedActivity ->
            currentUpdatedActivity?.let { currentUpdatedActivity ->
                val updatedDateTimeCalendar =
                    createCalendarFromTimeInMillis(currentUpdatedActivity.datetime)
                updatedDateTimeCalendar.apply {
                    if (date != null) {
                        set(Calendar.YEAR, date.year)
                        set(Calendar.MONTH, date.month)
                        set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                    }

                    if (time != null) {
                        set(Calendar.HOUR_OF_DAY, time.hourOfDay)
                        set(Calendar.MINUTE, time.minute)
                    }
                }

                currentUpdatedActivity.copy(
                    name = name ?: currentUpdatedActivity.name,
                    datetime = updatedDateTimeCalendar.timeInMillis
                )
            }
        }
    }

    fun saveUpdatedSelectedActivity() {
        updatedActivity.value?.let { updatedActivity ->
            update(updatedActivity)
        }
    }

    fun updateNewActivity(
        name: String? = null,
        date: SetDate? = null,
        time: SetTime? = null
    ) {
        if (name == null && date == null && time == null) return

        newActivity.update { currentNewActivity ->
            currentNewActivity.copy(
                name = name ?: currentNewActivity.name,
                date = date ?: currentNewActivity.date,
                time = time ?: currentNewActivity.time
            )
        }
    }

    fun saveNewActivity(onSucess: () -> Unit, onError: () -> Unit) {
        newActivity.value.let { newActivity ->
            if (newActivity.isFilled()) {
                insert(
                    name = newActivity.name.orEmpty(),
                    datetime = createNewActivityFilledCalendar().timeInMillis
                )

                this@PlannerActivityViewModel.newActivity.update { NewPlannerActivity() }

                onSucess()
            } else {
                onError()
            }
        }
    }

    private fun createNewActivityFilledCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        return calendar.apply {
            newActivity.value.let { newActivity ->
                set(Calendar.YEAR, newActivity.date?.year ?: 0)
                set(Calendar.MONTH, newActivity.date?.month ?: 0)
                set(Calendar.DAY_OF_MONTH, newActivity.date?.dayOfMonth ?: 0)
                set(Calendar.HOUR_OF_DAY, newActivity.time?.hourOfDay ?: 0)
                set(Calendar.MINUTE, newActivity.time?.minute ?: 0)
            }
        }
    }

    fun fetchActivities() {
        viewModelScope.launch {
            plannerActivityLocalDataSource.plannerActivities
                .flowOn(ioDispatcher)
                .collect { activities ->
                    withContext(mainDispatcher) {
                        _activities.value = activities
                    }
                }
        }
    }

    private fun insert(name: String, datetime: Long) {
        viewModelScope.launch {
            val plannerActivity = PlannerActivity(
                uuid = UUID.randomUUID().toString(),
                name = name,
                datetime = datetime,
                isCompleted = false
            )
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.insert(plannerActivity)
            }
        }
    }

    private fun update(updatedPlannerActivity: PlannerActivity) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.update(updatedPlannerActivity)
            }
        }
    }

    fun updateIsCompleted(uuid: String, isCompleted: Boolean) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.updateIsCompletedByUuid(uuid, isCompleted)
            }
        }
    }

    fun delete(uuid: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.deleteByUuid(uuid)
            }
        }
    }

}