package com.raktacare.util

import kotlin.String
import kotlin.random.Random

fun String.ifNotEmpty(block: (String) -> Unit) {
    if (this.isNotEmpty()) block(this)
}

fun generateRandomStringKey(length: Int = 16): String {
    val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val random = Random(System.currentTimeMillis())
    val keyBuilder = StringBuilder()
    for (i in 0 until length) {
        keyBuilder.append(alphabet[random.nextInt(alphabet.length)])
    }
    return keyBuilder.toString()
}