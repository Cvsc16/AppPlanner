package com.dev.caiovinicius.planner.data.datasource

import com.dev.caiovinicius.planner.data.database.PlannerActivityDao
import com.dev.caiovinicius.planner.domain.mapper.toDomain
import com.dev.caiovinicius.planner.domain.mapper.toEntity
import com.dev.caiovinicius.planner.domain.model.PlannerActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlannerActivityLocalDataSourceImpl(
    private val plannerActivityDao: PlannerActivityDao
): PlannerActivityLocalDataSource {
    override val plannerActivities: Flow<List<PlannerActivity>>
        get() = plannerActivityDao.getAll().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }

    override fun getByUuid(uuid: String): PlannerActivity {
        return plannerActivityDao.getByUuid(uuid).toDomain()
    }

    override fun updateIsCompletedByUuid(uuid: String, isCompleted: Boolean) {
        plannerActivityDao.updateIsCompletedByUuid(uuid, isCompleted)
    }

    override fun update(plannerActivity: PlannerActivity) {
        val entity = plannerActivityDao.getByUuid(plannerActivity.uuid)
        plannerActivityDao.update(plannerActivity.toEntity(entity.id))
    }

    override fun deleteByUuid(uuid: String) {
        plannerActivityDao.deleteByUuid(uuid)
    }

}