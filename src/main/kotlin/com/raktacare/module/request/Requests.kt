package com.raktacare.module.request

import com.raktacare.module.user.User
import org.jetbrains.exposed.sql.Table

object Requests : Table() {
    val requestKey = varchar("request_key", 128)
    val fullName = varchar("full_name", 128)
    val counterCode = varchar("counter_code", 128)
    val mobileNumber = varchar("mobile_number", 128)
    val email = varchar("email", 128)
    val locationKey = varchar("location", 128)
    val gender = enumeration("gender", User.Gender::class)
    val bloodGroup = enumeration("blood_group", User.BloodGroup::class)
    val units = integer("units")
    val urgency = enumeration("urgency", Request.RequestUrgency::class)
    val reason = enumeration("reason", Request.RequestReason::class)
    val note = varchar("note", 512)
    val requestedBy = varchar("requested_by", 128)
    val acceptedBy = varchar("accepted_by", 128)
    val requestType = enumeration("request_type", Request.RequestType::class)
    val status = enumeration("status", Request.RequestStatus::class)
    val requestTime = long("request_time")
    val acceptTime = long("accept_time")
    override val primaryKey = PrimaryKey(requestKey)
}