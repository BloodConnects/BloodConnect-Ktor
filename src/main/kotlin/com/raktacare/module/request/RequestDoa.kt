package com.raktacare.module.request

interface RequestDoa {
    suspend fun pushRequestKey(): String
    suspend fun checkRequestKey(requestKey: String): Boolean
    suspend fun addRequest(request: Request): Request?
    suspend fun updateRequest(request: Request): Boolean
    suspend fun deleteRequest(requestKey: String): Boolean
    suspend fun getRequests(): List<Request>
    suspend fun getRequestByRequestKey(requestKey: String): Request?
    suspend fun getRequestByUser(userUid: String): Request?
    suspend fun getRequestByStatus(status: Request.RequestStatus): List<Request>
    suspend fun getRequestByRequestType(requestType: Request.RequestType): List<Request>
    suspend fun getRequestByRequestUrgency(requestUrgency: Request.RequestUrgency): List<Request>
    suspend fun getRequestByRequestedBy(requestedBy: String): List<Request>
    suspend fun getRequestByAcceptedBy(acceptedBy: String): List<Request>
}