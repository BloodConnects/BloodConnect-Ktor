package com.raktacare.module.request

import com.raktacare.plugins.dbQuery
import com.raktacare.util.generateRandomStringKey
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RequestDaoImpl  {

     suspend fun pushRequestKey(): String {
        val key = generateRandomStringKey()
        if (checkRequestKey(key)) {
            return pushRequestKey()
        }
        return key
    }

     private suspend fun checkRequestKey(requestKey: String) = dbQuery {
        !Requests.select { Requests.requestKey eq requestKey }.empty()
    }

     suspend fun addRequest(request: Request) = dbQuery {
        val result = Requests.insert {
            it[requestKey] = request.requestKey
            it[fullName] = request.fullName
            it[counterCode] = request.counterCode
            it[mobileNumber] = request.mobileNumber
            it[email] = request.email
            it[locationKey] = request.locationKey
            it[gender] = request.gender
            it[bloodGroup] = request.bloodGroup
            it[units] = request.units
            it[urgency] = request.urgency
            it[reason] = request.reason
            it[note] = request.note
            it[requestedBy] = request.requestedBy
            it[acceptedBy] = request.acceptedBy
            it[requestType] = request.requestType
            it[status] = request.status
            it[requestTime] = request.requestTime
            it[acceptTime] = request.acceptTime
        }
        result.resultedValues?.singleOrNull()?.toRequest()
    }

     suspend fun updateRequest(request: Request) = dbQuery {
        Requests.update({ Requests.requestKey eq request.requestKey }) {
            it[requestKey] = request.requestKey
            it[fullName] = request.fullName
            it[counterCode] = request.counterCode
            it[mobileNumber] = request.mobileNumber
            it[email] = request.email
            it[locationKey] = request.locationKey
            it[gender] = request.gender
            it[bloodGroup] = request.bloodGroup
            it[units] = request.units
            it[urgency] = request.urgency
            it[reason] = request.reason
            it[note] = request.note
            it[requestedBy] = request.requestedBy
            it[acceptedBy] = request.acceptedBy
            it[requestType] = request.requestType
            it[status] = request.status
            it[requestTime] = request.requestTime
            it[acceptTime] = request.acceptTime
        } > 0
    }

     suspend fun deleteRequest(requestKey: String) = dbQuery {
        Requests.deleteWhere { Requests.requestKey eq requestKey } > 0
    }

     suspend fun getRequests() = dbQuery {
        Requests.selectAll().map { it.toRequest() }
    }

     suspend fun getRequestByRequestKey(requestKey: String) = dbQuery {
        Requests.select { Requests.requestKey eq requestKey }.singleOrNull()?.toRequest()
    }

     suspend fun getRequestByUser(userUid: String) = dbQuery {
        Requests.select { Requests.requestedBy eq userUid }.singleOrNull()?.toRequest()
    }

     suspend fun getRequestByStatus(status: Request.RequestStatus) = dbQuery {
        Requests.select { Requests.status eq status }.map { it.toRequest() }
    }

     suspend fun getRequestByRequestType(requestType: Request.RequestType) = dbQuery {
        Requests.select { Requests.requestType eq requestType }.map { it.toRequest() }
    }

     suspend fun getRequestByRequestUrgency(requestUrgency: Request.RequestUrgency) = dbQuery {
        Requests.select { Requests.urgency eq requestUrgency }.map { it.toRequest() }
    }

     suspend fun getRequestByRequestedBy(requestedBy: String) = dbQuery {
        Requests.select { Requests.requestedBy eq requestedBy }.map { it.toRequest() }
    }

     suspend fun getRequestByAcceptedBy(acceptedBy: String) = dbQuery {
        Requests.select { Requests.acceptedBy eq acceptedBy }.map { it.toRequest() }
    }

    private fun ResultRow.toRequest() = Request(
        requestKey = this[Requests.requestKey],
        fullName = this[Requests.fullName],
        counterCode = this[Requests.counterCode],
        mobileNumber = this[Requests.mobileNumber],
        email = this[Requests.email],
        locationKey = this[Requests.locationKey],
        gender = this[Requests.gender],
        bloodGroup = this[Requests.bloodGroup],
        units = this[Requests.units],
        urgency = this[Requests.urgency],
        reason = this[Requests.reason],
        note = this[Requests.note],
        requestedBy = this[Requests.requestedBy],
        acceptedBy = this[Requests.acceptedBy],
        requestType = this[Requests.requestType],
        status = this[Requests.status],
        requestTime = this[Requests.requestTime],
        acceptTime = this[Requests.acceptTime]
    )

}