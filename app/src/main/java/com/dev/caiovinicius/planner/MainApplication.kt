package com.dev.caiovinicius.planner

import android.app.Application
import com.dev.caiovinicius.planner.core.di.MainServiceLocator

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MainServiceLocator.initialize(this)
    }

}