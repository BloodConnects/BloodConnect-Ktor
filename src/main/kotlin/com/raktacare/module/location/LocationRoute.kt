package com.raktacare.module.location

import com.raktacare.module.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.locationRoute() {
    val locationDao = LocationDaoImpl()
    route("/Location") {
        authenticate("auth-jwt") {
            get {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid") ?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@get
                    }
                    locationDao.getLocationByUser(uid)?.let {
                        call.respond(BaseResponse(success = true, message = "Location Found", data = it))
                    } ?: run {
                        call.respond(BaseResponse(success = false, message = "Location Not Found", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
            get("/{locationKey}") {
                try {
                    val locationKey = call.parameters["locationKey"].toString()
                    locationDao.getLocationByLocationKey(locationKey)?.let {
                        call.respond(BaseResponse(success = true, message = "Location Found", data = it))
                    } ?: run {
                        call.respond(BaseResponse(success = false, message = "Location Not Found", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
            post {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid") ?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@post
                    }
                    var location = call.receive<Location>()
                    location = location.copy(locationKey = locationDao.pushLocationKey(), userUid = uid)
                    locationDao.addLocation(location)?.let {
                        call.respond(BaseResponse(success = true, message = "Location Add Successfully", data = it))
                    } ?: run {
                        call.respond(BaseResponse(success = false, message = "Location Add Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
            put {
                try {
                    var location = call.receive<Location>()
                    locationDao.getLocationByLocationKey(location.locationKey)?.let {
                        location = location.copy(userUid = it.userUid)
                        location.houseNo.ifEmpty { location = location.copy(houseNo = it.houseNo) }
                        location.street.ifEmpty { location = location.copy(street = it.street) }
                        location.address.ifEmpty { location = location.copy(address = it.address) }
                        location.city.ifEmpty { location = location.copy(city = it.city) }
                        location.state.ifEmpty { location = location.copy(state = it.state) }
                        location.country.ifEmpty { location = location.copy(country = it.country) }
                        location.postalCode.ifEmpty { location = location.copy(postalCode = it.postalCode) }
                        if (location.latitude == 0.0) location = location.copy(latitude = it.latitude)
                        if (location.longitude == 0.0) location = location.copy(longitude = it.longitude)
                        if (location.type == Location.Type.HOME) location = location.copy(type = it.type)
                    } ?: run {
                        call.respond(BaseResponse(success = false, message = "Location Not Found", data = null))
                        return@put
                    }
                    if (locationDao.updateLocation(location)) {
                        call.respond(BaseResponse(success = true, message = "Location Update Successfully", data = location))
                    } else {
                        call.respond(BaseResponse(success = false, message = "Location Update Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
            delete {
                try {
                    val locationKey = call.receive<Location>().locationKey
                    if (locationDao.deleteLocation(locationKey)) {
                        call.respond(BaseResponse(success = true, message = "Location Delete Successfully", data = null))
                    } else {
                        call.respond(BaseResponse(success = false, message = "Location Delete Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
        }
    }
}