package klog

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory

actual class LogVerifier {
    private val listAppender = ListAppender<ILoggingEvent>()
    private val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    
    actual fun startCapture() {
        listAppender.start()
        rootLogger.addAppender(listAppender)
    }
    
    actual fun stopCapture() {
        rootLogger.detachAppender(listAppender)
        listAppender.stop()
    }
    
    actual fun getCapturedMessages(): List<String> {
        return listAppender.list.map { it.formattedMessage }
    }
    
    actual fun clear() {
        listAppender.list.clear()
    }
    
    actual fun expectMessage(message: String): Boolean {
        return getCapturedMessages().any { it.contains(message) }
    }
}