package com.david.holamundo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform