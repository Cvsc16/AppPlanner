package com.dev.caiovinicius.planner.data.datasource

import com.dev.caiovinicius.planner.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface UserRegistrationLocalDataSource {

    fun getIsUserRegistered(): Boolean

    fun saveIsUserRegistered(isUserRegistered: Boolean)

    val profile: Flow<Profile>

    suspend fun saveProfile(profile: Profile)

}