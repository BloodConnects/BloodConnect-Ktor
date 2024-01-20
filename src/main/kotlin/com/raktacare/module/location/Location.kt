package com.raktacare.module.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationKey: String = "",
    val userUid: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val houseNo:String = "",
    val street: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: String = "",
    val type: Type = Type.HOME
) {
    @Serializable
    enum class Type {
        HOME, CURRENT
    }
}