package com.dev.caiovinicius.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dev.caiovinicius.planner.data.datasource.UserRegistrationLocalDataSource
import com.dev.caiovinicius.planner.data.di.MainServiceLocator

class UserRegistrationViewModel : ViewModel() {

    private val userRegistrationLocalDataSource : UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    fun getIsUserRegistered() : Boolean {
        return userRegistrationLocalDataSource.getIsUserRegistered()
    }

    fun saveIsUserRegistered(isUserRegistered : Boolean) {
        userRegistrationLocalDataSource.saveIsUserRegistered(isUserRegistered)
    }

}