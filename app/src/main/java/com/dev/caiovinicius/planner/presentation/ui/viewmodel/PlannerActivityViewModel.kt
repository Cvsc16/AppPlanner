package com.dev.caiovinicius.planner.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.caiovinicius.planner.core.di.MainServiceLocator
import com.dev.caiovinicius.planner.core.di.MainServiceLocator.ioDispatcher
import com.dev.caiovinicius.planner.core.di.MainServiceLocator.mainDispatcher
import com.dev.caiovinicius.planner.data.datasource.PlannerActivityLocalDataSource
import com.dev.caiovinicius.planner.domain.model.PlannerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
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

    fun fetchActivities() {
        viewModelScope.launch {
            launch {
                plannerActivityLocalDataSource.plannerActivities
                    .flowOn(ioDispatcher)
                    .collect { activities ->
                        withContext(mainDispatcher) {
                            _activities.value = activities
                        }
                    }
            }

            launch {
                delay(3_000)
                insert("Academia em grupo", Calendar.getInstance().timeInMillis)
                delay(3_000)
                insert("Treino de Futebol", Calendar.getInstance().timeInMillis)
                delay(3_000)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, 3)
                insert("Lavar louça", calendar.timeInMillis)
            }
        }
    }

    fun insert(name: String, datetime: Long) {
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

    fun update(updatedPlannerActivity: PlannerActivity) {
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