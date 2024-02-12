package com.raktacare.plugins

import com.raktacare.module.donor.donorRoute
import com.raktacare.module.location.locationRoute
import com.raktacare.module.request.requestRoute
import com.raktacare.module.update.updateRoute
import com.raktacare.module.user.userRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoute()
        locationRoute()
        donorRoute()
        requestRoute()
        updateRoute()
    }
}