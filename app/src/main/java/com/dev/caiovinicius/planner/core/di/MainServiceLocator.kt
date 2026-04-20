package com.dev.caiovinicius.planner.core.di

import android.app.Application
import androidx.room.Room
import com.dev.caiovinicius.planner.data.database.PLANNER_ACTIVITY_DATABASE_NAME
import com.dev.caiovinicius.planner.data.database.PlannerActivityDao
import com.dev.caiovinicius.planner.data.database.PlannerActivityDatabase
import com.dev.caiovinicius.planner.data.datasource.AuthenticationLocalDataSource
import com.dev.caiovinicius.planner.data.datasource.AuthenticationLocalDataSourceImpl
import com.dev.caiovinicius.planner.data.datasource.PlannerActivityLocalDataSource
import com.dev.caiovinicius.planner.data.datasource.PlannerActivityLocalDataSourceImpl
import com.dev.caiovinicius.planner.data.datasource.UserRegistrationLocalDataSource
import com.dev.caiovinicius.planner.data.datasource.UserRegistrationLocalDataSourceImpl
import kotlinx.coroutines.Dispatchers

object MainServiceLocator {

    private var _application: Application? = null
    private val application: Application
        get() = _application!!

    val ioDispatcher by lazy {
        Dispatchers.IO
    }

    val mainDispatcher by lazy {
        Dispatchers.Main
    }

    val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        UserRegistrationLocalDataSourceImpl(application.applicationContext)
    }

    val authenticationLocalDataSource: AuthenticationLocalDataSource by lazy {
        AuthenticationLocalDataSourceImpl(application.applicationContext)
    }

    val plannerActivityDao: PlannerActivityDao by lazy {
        val database = Room.databaseBuilder(
            application.applicationContext,
            PlannerActivityDatabase::class.java,
            PLANNER_ACTIVITY_DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

        database.plannerActivityDao()
    }

    val plannerActivityLocalDataSource: PlannerActivityLocalDataSource by lazy {
        PlannerActivityLocalDataSourceImpl(plannerActivityDao)
    }

    fun initialize(application: Application) {
        _application = application
    }

    fun clear() {
        _application = null
    }

}