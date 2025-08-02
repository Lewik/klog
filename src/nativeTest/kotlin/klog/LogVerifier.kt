package klog

import kotlin.concurrent.atomics.AtomicReference

actual class LogVerifier {
    private val capturedMessages = mutableListOf<String>()
    private var isCapturing = false
    private var originalWriter: ((String) -> Unit)? = null

    actual fun startCapture() {
        isCapturing = true
        capturedMessages.clear()
        
        // Store original writer and replace with capturing one
        originalWriter = NativeLoggerRegistry.currentWriter
        NativeLoggerRegistry.currentWriter = { message ->
            capturedMessages.add(message)
        }
    }

    actual fun stopCapture() {
        isCapturing = false
        
        // Restore original writer
        originalWriter?.let { writer ->
            NativeLoggerRegistry.currentWriter = writer
        }
        originalWriter = null
    }

    actual fun getCapturedMessages(): List<String> {
        return capturedMessages.toList()
    }

    actual fun clear() {
        capturedMessages.clear()
    }

    actual fun expectMessage(message: String): Boolean {
        return capturedMessages.any { it.contains(message) }
    }
}