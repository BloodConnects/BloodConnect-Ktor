package com.raktacare.module.donor

import com.raktacare.module.BaseResponse
import com.raktacare.module.location.LocationDaoImpl
import com.raktacare.module.user.User
import com.raktacare.module.user.UserDaoImpl
import com.raktacare.util.toPage
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
        return userDao.getUserByBloodRequest(query.bloodGroup).filter { user ->
            locationDao.getLocationByLocationKey(user.locationKey)?.let { location ->
                location.city == query.location.city && location.state == query.location.state && location.country == query.location.country
            } ?: run {
                false
            }
        }.toPage(query).map {
            it.copy(location = locationDao.getLocationByLocationKey(it.locationKey))
        }
    } else if (query.bloodGroup != null) {
        return userDao.getUserByBloodRequest(query.bloodGroup).toPage(query).map {
            it.copy(location = locationDao.getLocationByLocationKey(it.locationKey))
        }
    } else if (query.location != null) {
        return userDao.getUsers().filter { user ->
            locationDao.getLocationByLocationKey(user.locationKey)?.let { location ->
                location.city == query.location.city && location.state == query.location.state && location.country == query.location.country
            } ?: run {
                false
            }
        }.toPage(query).map {
            it.copy(location = locationDao.getLocationByLocationKey(it.locationKey))
        }
    } else {
        return userDao.getUsers().toPage(query).map {
            it.copy(location = locationDao.getLocationByLocationKey(it.locationKey))
        }
    }
}