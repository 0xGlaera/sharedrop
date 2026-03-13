package org.nitish.project.sharedrop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getDeviceName(): String
