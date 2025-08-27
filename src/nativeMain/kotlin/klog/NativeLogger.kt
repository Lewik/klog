package klog

class NativeLogger(private val name: String) : BaseLogger {
    private fun log(level: LogLevel, message: Any?, throwable: Throwable? = null) {
        KLoggerRegistry.currentWriter.write(level, name, message.toString(), throwable)
    }

    override fun trace(message: Any?) = log(LogLevel.TRACE, message)
    override fun debug(message: Any?) = log(LogLevel.DEBUG, message)
    override fun info(message: Any?) = log(LogLevel.INFO, message)
    override fun warn(message: Any?) = log(LogLevel.WARN, message)
    override fun error(message: Any?) = log(LogLevel.ERROR, message)

    override fun trace(t: Throwable, message: Any?) = log(LogLevel.TRACE, message, t)
    override fun debug(t: Throwable, message: Any?) = log(LogLevel.DEBUG, message, t)
    override fun info(t: Throwable, message: Any?) = log(LogLevel.INFO, message, t)
    override fun warn(t: Throwable, message: Any?) = log(LogLevel.WARN, message, t)
    override fun error(t: Throwable, message: Any?) = log(LogLevel.ERROR, message, t)
}

actual object KLoggerRegistry {
    actual var currentWriter: LogWriter = LogWriter { level, tag, message, throwable ->
        if (throwable != null) {
            println("$level $tag: $message - ${throwable.message}")
        } else {
            println("$level $tag: $message")
        }
    }
}