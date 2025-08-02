package klog

class NativeLogger(private val name: String) : BaseLogger {
    override val isTraceEnabled: Boolean = true
    override val isDebugEnabled: Boolean = true
    override val isInfoEnabled: Boolean = true
    override val isWarnEnabled: Boolean = true
    override val isErrorEnabled: Boolean = true

    override fun trace(message: Any?) {
        println("TRACE $name: $message")
    }

    override fun debug(message: Any?) {
        println("DEBUG $name: $message")
    }

    override fun info(message: Any?) {
        println("INFO $name: $message")
    }

    override fun warn(message: Any?) {
        println("WARN $name: $message")
    }

    override fun error(message: Any?) {
        println("ERROR $name: $message")
    }

    override fun trace(t: Throwable, message: Any?) {
        println("TRACE $name: $message - ${t.message}")
    }

    override fun debug(t: Throwable, message: Any?) {
        println("DEBUG $name: $message - ${t.message}")
    }

    override fun info(t: Throwable, message: Any?) {
        println("INFO $name: $message - ${t.message}")
    }

    override fun warn(t: Throwable, message: Any?) {
        println("WARN $name: $message - ${t.message}")
    }

    override fun error(t: Throwable, message: Any?) {
        println("ERROR $name: $message - ${t.message}")
    }
}