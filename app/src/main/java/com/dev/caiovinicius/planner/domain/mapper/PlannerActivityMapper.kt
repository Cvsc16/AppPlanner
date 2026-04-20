package com.dev.caiovinicius.planner.domain.mapper

import com.dev.caiovinicius.planner.data.database.PlannerActivityEntity
import com.dev.caiovinicius.planner.domain.model.PlannerActivity

fun PlannerActivityEntity.toDomain() : PlannerActivity =
    PlannerActivity(
        this.uuid,
        this.name,
        this.datetime,
        this.isCompleted
    )

fun PlannerActivity.toEntity(id: Int) : PlannerActivityEntity =
    PlannerActivityEntity(
        id,
        this.uuid,
        this.name,
        this.datetime,
        this.isCompleted
    )