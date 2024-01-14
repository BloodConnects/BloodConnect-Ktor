package com.raktacare.module.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.raktacare.module.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoute() {
    val userDao = UserDaoImpl()
    route("User") {
        post {
            try {
                var user = call.receive<User>()
                user.uid.ifEmpty {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse(success = false, message = "User Id is Empty", data = null)
                    )
                    return@post
                }
                userDao.getUserByUID(user.uid)?.let {
                    call.respond(BaseResponse(success = true, message = "User Already Exist", data = null))
                    return@post
                }

                val userToken = generateUserToken(call.application.environment.config, user)
                user = user.copy(userToken = userToken)

                userDao.addUser(user)?.let {
                    call.respond(BaseResponse(success = true, message = "User Add Successfully", data = it))
                } ?: run {
                    call.respond(BaseResponse(success = false, message = "User Add Failed", data = null))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BaseResponse(success = false, message = e.message?:"Internal Server Error", data = null)
                )
                e.printStackTrace()
            }
        }
        authenticate("auth-jwt") {
            get("/GetAll") {
                val users = userDao.getUsers()
                call.respond(BaseResponse(success = true, data = users))
            }
            get {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid")?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@get
                    }
                    userDao.getUserByUID(uid)?.let {
                        call.respond(BaseResponse(success = true, message = "User Details", data = it))
                    }?: run {
                        call.respond(
                            HttpStatusCode.NotFound,
                            BaseResponse(success = false, message = "User Not Found", data = null)
                        )
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        BaseResponse(success = false, message = e.message?:"Internal Server Error", data = null)
                    )
                    e.printStackTrace()
                }
            }
            put {
                try {
                    var user = call.receive<User>()
                    if (call.principal<JWTPrincipal>()?.get("uid") != user.uid) {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@put
                    }
                    userDao.getUserByUID(user.uid)?.let {
                        user.fullName.ifEmpty { user = user.copy(fullName = it.fullName) }
                        user.mobileNumber.ifEmpty { user = user.copy(mobileNumber = it.mobileNumber) }
                        user.countryCode.ifEmpty { user = user.copy(countryCode = it.countryCode) }
                        user.mailAddress.ifEmpty { user = user.copy(mailAddress = it.mailAddress) }
                        user.profilePictureUrl.ifEmpty { user = user.copy(profilePictureUrl = it.profilePictureUrl) }
                        user.deviceToken.ifEmpty { user = user.copy(deviceToken = it.deviceToken) }
                        user.userToken.ifEmpty { user = user.copy(userToken = it.userToken) }
                        if (user.bloodGroup==User.BloodGroup.unknown) user = user.copy(bloodGroup = it.bloodGroup)
                        if (user.gender==User.Gender.Other) user = user.copy(gender = it.gender)
                    }
                    if (userDao.updateUser(user)) {
                        call.respond(BaseResponse(success = true, message = "User Update Successfully", data = user))
                    } else {
                        call.respond(BaseResponse(success = false, message = "User Update Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        BaseResponse(success = false, message = e.message?:"Internal Server Error", data = null)
                    )
                    e.printStackTrace()
                }
            }
            delete {
                try {
                    val uid = call.principal<JWTPrincipal>()?.get("uid") ?: run {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(success = false, message = "Unauthorized", data = null)
                        )
                        return@delete
                    }
                    if (userDao.deleteUser(uid)) {
                        call.respond(BaseResponse(success = true, message = "User Delete Successfully", data = null))
                    } else {
                        call.respond(BaseResponse(success = false, message = "User Delete Failed", data = null))
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        BaseResponse(success = false, message = e.message?:"Internal Server Error", data = null)
                    )
                    e.printStackTrace()
                }
            }
        }
    }

}

fun generateUserToken(config: ApplicationConfig, user: User): String {
    val secret = config.property("ktor.jwt.secret").getString()
    val issuer = config.property("ktor.jwt.issuer").getString()
    val audience = config.property("ktor.jwt.audience").getString()
    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("uid", user.uid)
        .withClaim("countryCode", user.countryCode)
        .withClaim("mobileNumber", user.mobileNumber)
        .withExpiresAt(Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time)
        .sign(Algorithm.HMAC256(secret))
}

