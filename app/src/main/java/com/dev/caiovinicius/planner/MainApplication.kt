package com.dev.caiovinicius.planner

import android.app.Application
import com.dev.caiovinicius.planner.data.di.MainServiceLocator

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MainServiceLocator.initialize(this)
    }

}