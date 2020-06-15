package klog

class NativeLogger(private val level: KLoggingLevels,
                   private val formatter: Formatter,
                   private val writer: (String) -> Any) : BaseLogger {
    override val isTraceEnabled: Boolean get() = level >= KLoggingLevels.TRACE
    override val isDebugEnabled: Boolean get() = level >= KLoggingLevels.DEBUG
    override val isInfoEnabled: Boolean get() = level >= KLoggingLevels.INFO
    override val isWarnEnabled: Boolean get() = level >= KLoggingLevels.WARN
    override val isErrorEnabled: Boolean get() = level >= KLoggingLevels.ERROR

    override fun trace(message: Any?) {
        if (isTraceEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.TRACE, null, message))
        }
    }

    override fun debug(message: Any?) {
        if (isDebugEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.DEBUG, null, message))
        }
    }

    override fun info(message: Any?) {
        if (isInfoEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.INFO, null, message))
        }
    }

    override fun warn(message: Any?) {
        if (isWarnEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.WARN, null, message))
        }
    }

    override fun error(message: Any?) {
        if (isErrorEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.ERROR, null, message))
        }
    }

    override fun trace(t: Throwable, message: Any?) {
        if (isTraceEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.TRACE, t, message))
        }
    }

    override fun debug(t: Throwable, message: Any?) {
        if (isDebugEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.DEBUG, t, message))
        }
    }

    override fun info(t: Throwable, message: Any?) {
        if (isInfoEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.INFO, t, message))
        }
    }

    override fun warn(t: Throwable, message: Any?) {
        if (isWarnEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.WARN, t, message))
        }
    }

    override fun error(t: Throwable, message: Any?) {
        if (isErrorEnabled) {
            writer.invoke(formatter.formatMessage(KLoggingLevels.ERROR, t, message))
        }
    }
}
