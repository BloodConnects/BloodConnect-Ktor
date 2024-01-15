package com.raktacare.module

import kotlinx.serialization.Serializable

@Serializable
open class BasePagination(
    @Serializable val offset: Int = 0,
    @Serializable val limit: Int = 10
)