package com.raktacare.module.donor

import com.raktacare.module.BaseResponse
import com.raktacare.module.donor.distance.DistanceResponse
import com.raktacare.module.location.Location
import com.raktacare.module.location.LocationDaoImpl
import com.raktacare.module.user.User
import com.raktacare.module.user.UserDaoImpl
import com.raktacare.util.toPage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Routing.donorRoute() {
    val userDao = UserDaoImpl()
    val locationDao = LocationDaoImpl()
    route("Donor") {
        get {
            try {
                call.respond(BaseResponse(success = true, message = "All Donor List", data = userDao.getUsers()))
            } catch (e: Exception) {
                call.respondText(e.message ?: "Internal Server Error")
                e.printStackTrace()
            }
        }
        post {
            try {
                val query = call.receive<DonorPagination>()
                call.respond(
                    BaseResponse(
                        success = true,
                        message = "Donor List",
                        data = getDonors(query, userDao, locationDao)
                    )
                )
            } catch (e: Exception) {
                call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                e.printStackTrace()
            }

        }
    }
}

suspend fun getDonors(query: DonorPagination, userDao: UserDaoImpl, locationDao: LocationDaoImpl): List<User> {
    if (query.bloodGroup != null && query.location != null) {
        return userDao.getUserByBloodRequest(query.bloodGroup).toPage(query).map {
            it.copy(location = locationDao.getLocationByUser(it.uid), distanceInfo = getDistanceInfo(query.location, locationDao.getLocationByUser(it.uid)))
        }.toSortByDistance()
    } else if (query.bloodGroup != null) {
        return userDao.getUserByBloodRequest(query.bloodGroup).toPage(query).map {
            it.copy(location = locationDao.getLocationByUser(it.uid))
        }
    } else if (query.location != null) {
        return userDao.getUsers().toPage(query).map {
            it.copy(location = locationDao.getLocationByUser(it.uid), distanceInfo = getDistanceInfo(query.location, locationDao.getLocationByUser(it.uid)))
        }.toSortByDistance()
    } else {
        return userDao.getUsers().toPage(query).map {
            it.copy(location = locationDao.getLocationByUser(it.uid))
        }
    }
}

private fun List<User>.toSortByDistance(): List<User> {
    return sortedBy { it.distanceInfo?.distance }.reversed()
}

suspend fun getDistanceInfo(fromLocation: Location, toLocation: Location?): DistanceResponse.DistanceInfo? {
    if (toLocation == null || fromLocation == toLocation) {
        return null
    }
    val mapKey = System.getenv("MAP_API_KEY")
    val distanceUri =
        "https://maps.googleapis.com/maps/api/directions/json?destination=${toLocation.latitude},${toLocation.longitude}&origin=${fromLocation.latitude},${fromLocation.longitude}&key=$mapKey"
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    return httpClient.get(distanceUri).body<DistanceResponse>().toDistanceInfo()
}