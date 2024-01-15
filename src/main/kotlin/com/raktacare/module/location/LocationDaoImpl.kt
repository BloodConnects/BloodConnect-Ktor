package com.raktacare.module.location

import com.raktacare.util.generateRandomStringKey
import com.raktacare.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LocationDaoImpl : LocationDao {

    override suspend fun pushLocationKey(): String {
        val key = generateRandomStringKey()
        if (checkLocationKey(key)) {
            return pushLocationKey()
        }
        return key
    }

    private suspend fun checkLocationKey(messageKey: String) = dbQuery {
        !Locations.select { Locations.locationKey eq messageKey }.empty()
    }

    override suspend fun addLocation(location: Location) = dbQuery {
        val result = Locations.insert {
            it[locationKey] = location.locationKey
            it[latitude] = location.latitude
            it[longitude] = location.longitude
            it[address] = location.address
            it[city] = location.city
            it[state] = location.state
            it[country] = location.country
            it[postalCode] = location.postalCode
            it[type] = location.type
        }
        result.resultedValues?.singleOrNull()?.toLocation()
    }

    override suspend fun updateLocation(location: Location) = dbQuery {
        Locations.update({ Locations.locationKey eq location.locationKey }) {
            it[latitude] = location.latitude
            it[longitude] = location.longitude
            it[address] = location.address
            it[city] = location.city
            it[state] = location.state
            it[country] = location.country
            it[postalCode] = location.postalCode
            it[type] = location.type
        } > 0
    }

    override suspend fun deleteLocation(locationKey: String) = dbQuery {
        Locations.deleteWhere { Locations.locationKey eq locationKey } > 0
    }

    override suspend fun getLocations() = dbQuery {
        Locations.selectAll().map { it.toLocation() }
    }

    override suspend fun getLocationByLocationKey(locationKey: String) = dbQuery {
        Locations.select { Locations.locationKey eq locationKey }.singleOrNull()?.toLocation()
    }

    override suspend fun getLocationByCity(city: String) = dbQuery {
        Locations.select { Locations.city eq city }.map { it.toLocation() }
    }

    private fun ResultRow.toLocation() = Location(
        locationKey = this[Locations.locationKey],
        latitude = this[Locations.latitude],
        longitude = this[Locations.longitude],
        address = this[Locations.address],
        city = this[Locations.city],
        state = this[Locations.state],
        country = this[Locations.country],
        postalCode = this[Locations.postalCode],
        type = this[Locations.type]
    )

}