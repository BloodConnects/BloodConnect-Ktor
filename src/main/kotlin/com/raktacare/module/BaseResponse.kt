package com.raktacare.module

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null,
)