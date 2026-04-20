package com.dev.caiovinicius.planner.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannerActivityDao {

    @Query("SELECT * FROM planneractivityentity ORDER BY is_completed AND datetime")
    fun getAll(): Flow<List<PlannerActivityEntity>>

    @Insert
    fun insert(plannerActivityEntity: PlannerActivityEntity)

    @Query("SELECT * FROM planneractivityentity WHERE uuid = :uuid")
    fun getByUuid(uuid: String): PlannerActivityEntity

    @Query("UPDATE planneractivityentity SET is_completed = :isCompleted WHERE uuid = :uuid")
    fun updateIsCompletedByUuid(uuid: String, isCompleted: Boolean)

    @Update
    fun update(plannerActivityEntity: PlannerActivityEntity)

    @Query("DELETE FROM planneractivityentity WHERE uuid = :uuid")
    fun deleteByUuid(uuid: String)

}