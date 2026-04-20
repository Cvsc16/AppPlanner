package com.dev.caiovinicius.planner.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlannerActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uuid: String,
    val name: String,
    val datetime: Long,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean
)
