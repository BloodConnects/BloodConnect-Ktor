package com.raktacare.util

import com.raktacare.module.BasePagination
import kotlin.math.min

fun <T> List<T>.toPage(pagination: BasePagination): List<T> {
    if (pagination.offset < 0 || pagination.limit < 0) {
        throw IllegalArgumentException("Offset or Limit cannot be less than 0")
    }
    if (pagination.offset > size) {
        throw IllegalArgumentException("Offset cannot be greater than size")
    }
    if (pagination.limit == 0) {
        return emptyList()
    }
    return subList(pagination.offset, min(pagination.offset + pagination.limit,size))
}