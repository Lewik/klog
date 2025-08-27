package klog

fun interface LogWriter {
    fun write(level: LogLevel, tag: String, message: String, throwable: Throwable?)
}