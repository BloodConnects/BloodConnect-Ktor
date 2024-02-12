package com.raktacare.module.request

import com.raktacare.module.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val requestKey: String = "",
    val fullName: String = "",
    val counterCode: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val locationKey: String = "",
    val gender: User.Gender = User.Gender.Other,
    val bloodGroup: User.BloodGroup = User.BloodGroup.unknown,
    val units: Int = 0,
    val urgency: RequestUrgency = RequestUrgency.NORMAL,
    val reason: RequestReason = RequestReason.OTHER,
    val note: String = "",
    val requestedBy: String = "",
    val acceptedBy: String = "",
    val requestType: RequestType = RequestType.BLOOD,
    val status: RequestStatus = RequestStatus.PENDING,
    val requestTime: Long = 0L,
    val acceptTime: Long = 0L
) {

    @Serializable
    enum class RequestType {
        BLOOD, PLASMA, ORGAN
    }

    @Serializable
    enum class RequestStatus {
        PENDING, ACCEPTED, REJECTED
    }

    @Serializable
    enum class RequestUrgency {
        URGENT, MEDIUM, NORMAL
    }

    @Serializable
    enum class RequestReason {
        COVID, DENGUE, ACCIDENT, SURGERY, OTHER
    }

}

