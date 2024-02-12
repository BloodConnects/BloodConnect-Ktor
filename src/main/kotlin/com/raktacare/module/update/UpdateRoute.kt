package com.raktacare.module.update

import com.raktacare.module.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.updateRoute() {
    val updateDao = UpdateDao()
    route("Update") {
        authenticate("auth-jwt") {

            get("All") {
                try {
                    call.respond(BaseResponse(success = true, message = "All Update List", data = updateDao.getUpdates()))
                } catch (e: Exception) {
                    call.respondText(e.message ?: "Internal Server Error")
                    e.printStackTrace()
                }
            }

            get {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid") ?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@get
                    }
                    updateDao.getUpdatesByUserUid(uid).ifEmpty {
                        call.respond(BaseResponse(success = true, message = "Update Found", data = it))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }

            get("/{updateKey}") {
                try {
                    val updateKey = call.parameters["updateKey"].toString()
                    updateDao.getUpdateByKey(updateKey)?.let {
                        call.respond(BaseResponse(success = true, message = "Update Found", data = it))
                    } ?: run {
                        call.respond(BaseResponse(success = false, message = "Update Not Found", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }

            get("Type/{updateType}") {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid") ?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@get
                    }
                    val updateType = Update.UpdateType.valueOf(call.parameters["updateType"].toString())
                    updateDao.getUpdatesByUpdateType(uid,updateType).ifEmpty {
                        call.respond(BaseResponse(success = true, message = "Update Found", data = it))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }

            post {
                try {
                    val update = call.receive<Update>().copy(updateKey = updateDao.pushUpdateKey())
                    call.respond(
                        BaseResponse(
                            success = true,
                            message = "Update Add Successfully",
                            data = updateDao.addUpdate(update)
                        )
                    )
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }

            put {
                try {
                    val update = call.receive<Update>()
                    if (updateDao.updateUpdate(update)) {
                        call.respond(
                            BaseResponse(
                                success = true,
                                message = "Update Update Successfully",
                                data = update
                            )
                        )
                    } else {
                        call.respond(BaseResponse(success = false, message = "Update Update Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }

            delete("/{updateKey}") {
                try {
                    val updateKey = call.parameters["updateKey"].toString()
                    call.respond(
                        BaseResponse(
                            success = true,
                            message = "Update Delete Successfully",
                            data = updateDao.deleteUpdate(updateKey)
                        )
                    )
                } catch (e: Exception) {
                    call.respond(BaseResponse(success = false, message = e.message ?: "Internal Server Error", data = null))
                    e.printStackTrace()
                }
            }
        }
    }
}