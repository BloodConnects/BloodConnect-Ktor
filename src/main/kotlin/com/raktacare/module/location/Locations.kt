package com.raktacare.module.location

import org.jetbrains.exposed.sql.Table

object Locations : Table() {
    val locationKey = varchar("location_key", 128)
    val userUid = varchar("user_uid", 128)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val houseNo = varchar("house_no", 128)
    val street = varchar("street", 128)
    val address = varchar("address", 512)
    val city = varchar("city", 128)
    val state = varchar("state", 128)
    val country = varchar("country", 128)
    val postalCode = varchar("postal_code", 128)
    val type = enumeration("type", Location.Type::class)
    override val primaryKey = PrimaryKey(locationKey)
}