package com.dev.caiovinicius.planner.data.datasource

import com.dev.caiovinicius.planner.domain.model.PlannerActivity
import kotlinx.coroutines.flow.Flow

interface PlannerActivityLocalDataSource {
    val plannerActivities: Flow<List<PlannerActivity>>

    fun getByUuid(uuid: String): PlannerActivity

    fun updateIsCompletedByUuid(uuid: String, isCompleted: Boolean)

    fun update(plannerActivity: PlannerActivity)

    fun deleteByUuid(uuid: String)

}