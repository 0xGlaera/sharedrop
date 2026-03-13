package org.nitish.project.sharedrop

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun getDeviceName(): String = "WebJS-Browser"
