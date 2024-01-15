package com.raktacare.module.location

interface LocationDao {
    suspend fun pushLocationKey(): String
    suspend fun addLocation(location: Location): Location?
    suspend fun updateLocation(location: Location): Boolean
    suspend fun deleteLocation(locationKey: String): Boolean
    suspend fun getLocations(): List<Location>
    suspend fun getLocationByLocationKey(locationKey: String): Location?
    suspend fun getLocationByCity(city: String): List<Location>
}