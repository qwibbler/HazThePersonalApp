package com.example.hazthepersonalapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform