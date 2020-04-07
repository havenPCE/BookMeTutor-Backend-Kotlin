package com.pce.kotlin.bookmetutor.util

import kotlin.random.Random

fun Random.nextString(length: Int = 10): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length).map { characters[nextInt(0, characters.length)] }.joinToString { "" }
}