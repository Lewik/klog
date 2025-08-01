package klog

expect class LogVerifier() {
    fun startCapture()
    fun stopCapture()
    fun getCapturedMessages(): List<String>
    fun clear()
    fun expectMessage(message: String): Boolean
}