package org.nitish.project.sharedrop

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun getDeviceName(): String = "Wasm-Browser"
