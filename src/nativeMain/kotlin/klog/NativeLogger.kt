package klog

class NativeLogger(private val name: String) : BaseLogger {
    override val isTraceEnabled: Boolean = true
    override val isDebugEnabled: Boolean = true
    override val isInfoEnabled: Boolean = true
    override val isWarnEnabled: Boolean = true
    override val isErrorEnabled: Boolean = true

    private fun log(level: String, message: Any?) {
        NativeLoggerRegistry.currentWriter("$level $name: $message")
    }

    private fun log(level: String, t: Throwable, message: Any?) {
        NativeLoggerRegistry.currentWriter("$level $name: $message - ${t.message}")
    }

    override fun trace(message: Any?) = log("TRACE", message)
    override fun debug(message: Any?) = log("DEBUG", message)
    override fun info(message: Any?) = log("INFO", message)
    override fun warn(message: Any?) = log("WARN", message)
    override fun error(message: Any?) = log("ERROR", message)

    override fun trace(t: Throwable, message: Any?) = log("TRACE", t, message)
    override fun debug(t: Throwable, message: Any?) = log("DEBUG", t, message)
    override fun info(t: Throwable, message: Any?) = log("INFO", t, message)
    override fun warn(t: Throwable, message: Any?) = log("WARN", t, message)
    override fun error(t: Throwable, message: Any?) = log("ERROR", t, message)
}

// Global registry for native logger writer injection (shared between main and test)
object NativeLoggerRegistry {
    var currentWriter: (String) -> Unit = { message -> println(message) }
}