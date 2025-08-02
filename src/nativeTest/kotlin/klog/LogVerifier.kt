package klog

actual class LogVerifier {
    private val capturedMessages = mutableListOf<String>()
    private var isCapturing = false

    actual fun startCapture() {
        isCapturing = true
        capturedMessages.clear()
    }

    actual fun stopCapture() {
        isCapturing = false
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