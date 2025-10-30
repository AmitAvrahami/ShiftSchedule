package com.example.smartschedule.domain.models.user

import com.example.smartschedule.domain.models.user.roles.Role

data class User(
    val id: String,
    val fullName: String,
    val nationalId: String, // todo : validate national id
    val role: Role,
    val isActive: Boolean = true
){
    fun deaActivateUser() = this.copy(isActive = false)
    fun activateUser() = this.copy(isActive = true)

}