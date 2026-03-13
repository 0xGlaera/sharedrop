package org.nitish.project.sharedrop

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getDeviceName(): String = "Mac-" + System.getProperty("user.name")
