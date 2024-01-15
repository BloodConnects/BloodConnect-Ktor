package com.raktacare.plugins

import com.raktacare.module.BasePagination
import com.raktacare.module.BaseResponse
import com.raktacare.module.donor.donorRoute
import com.raktacare.module.location.Location
import com.raktacare.module.location.LocationDaoImpl
import com.raktacare.module.user.User
import com.raktacare.module.user.UserDaoImpl
import com.raktacare.module.user.userRoute
import com.raktacare.util.toPage
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoute()
        donorRoute()
    }
}