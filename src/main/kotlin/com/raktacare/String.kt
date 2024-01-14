package com.raktacare

import kotlin.String

fun String.ifNotEmpty(block: (String) -> Unit) {
    if (this.isNotEmpty()) block(this)
}