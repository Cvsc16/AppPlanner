package com.dev.caiovinicius.planner.data.datasource

interface UserRegistrationLocalDataSource {

    fun getIsUserRegistered(): Boolean

    fun saveIsUserRegistered(isUserRegistered: Boolean)

}