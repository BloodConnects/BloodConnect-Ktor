package com.raktacare.module.request

import com.raktacare.module.BaseResponse
import com.raktacare.module.location.LocationDaoImpl
import com.raktacare.module.update.Update
import com.raktacare.module.update.UpdateDao
import com.raktacare.module.user.UserDaoImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.requestRoute() {
    val requestDao = RequestDaoImpl()
    val updateDao = UpdateDao()
    val userDao = UserDaoImpl()
    val locationDao = LocationDaoImpl()

    route("/Request") {
        get {
            try {
                call.respond(BaseResponse(success = true, message = "All Request List", data = requestDao.getRequests()))
            } catch (e: Exception) {
                call.respondText(e.message ?: "Internal Server Error")
                e.printStackTrace()
            }
        }
        post {
            try {
                val request = call.receive<Request>().copy(requestKey = requestDao.pushRequestKey(), requestTime = System.currentTimeMillis())
                call.respond(
                    BaseResponse(
                        success = true,
                        message = "Request Add Successfully",
                        data = requestDao.addRequest(request)
                    )
                )
                val requestedUser = userDao.getUserByUID(request.requestedBy)
                val requestedUserLocation = locationDao.getLocationByLocationKey(request.locationKey)
                updateDao.addUpdate(
                    Update(
                        updateKey = updateDao.pushUpdateKey(),
                        updateImage = requestedUser?.profilePictureUrl?:"",
                        userUid = request.requestedBy,
                        referenceKey = request.requestKey,
                        updateType = Update.UpdateType.REQUEST,
                        title = "Request for ${request.bloodGroup} Blood Group",
                        description = "${requestedUser?.fullName?:"Someone"} requested for ${request.bloodGroup} blood group form ${requestedUserLocation?.city?:"Unknown"}",
                        updateDate = request.requestTime
                    )
                )
            } catch (e: Exception) {
                call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                e.printStackTrace()
            }
        }
        get("/{requestKey}") {
            try {
                val requestKey = call.parameters["requestKey"].toString()
                requestDao.getRequestByRequestKey(requestKey)?.let {
                    call.respond(BaseResponse(success = true, message = "Request Found", data = it))
                } ?: run {
                    call.respond(BaseResponse(success = false, message = "Request Not Found", data = null))
                }
            } catch (e: Exception) {
                call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                e.printStackTrace()
            }
        }
        put {
            try {
                val request = call.receive<Request>()
                call.respond(
                    BaseResponse(
                        success = true,
                        message = "Request Update Successfully",
                        data = requestDao.updateRequest(request)
                    )
                )
            } catch (e: Exception) {
                call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                e.printStackTrace()
            }
        }
        delete("/{requestKey}") {
            try {
                val requestKey = call.parameters["requestKey"].toString()
                call.respond(
                    BaseResponse(
                        success = true,
                        message = "Request Delete Successfully",
                        data = requestDao.deleteRequest(requestKey)
                    )
                )
            } catch (e: Exception) {
                call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                e.printStackTrace()
            }
        }
    }
}