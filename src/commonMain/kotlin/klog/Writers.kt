package klog

object Writers {
    
    fun console(): (LogLevel, String, String, Throwable?) -> Unit = { level, tag, message, throwable ->
        if (throwable != null) {
            println("$level $tag: $message - ${throwable.message}")
        } else {
            println("$level $tag: $message")
        }
    }
    
    fun filtering(
        minLevel: LogLevel,
        writer: (LogLevel, String, String, Throwable?) -> Unit = console()
    ): (LogLevel, String, String, Throwable?) -> Unit = { level, tag, message, throwable ->
        if (level.ordinal >= minLevel.ordinal) {
            writer(level, tag, message, throwable)
        }
    }
    
    fun tagFiltering(
        tagPattern: Regex,
        writer: (LogLevel, String, String, Throwable?) -> Unit = console()
    ): (LogLevel, String, String, Throwable?) -> Unit = { level, tag, message, throwable ->
        if (tagPattern.matches(tag)) {
            writer(level, tag, message, throwable)
        }
    }
    
    fun combining(
        vararg writers: (LogLevel, String, String, Throwable?) -> Unit
    ): (LogLevel, String, String, Throwable?) -> Unit = { level, tag, message, throwable ->
        writers.forEach { writer ->
            writer(level, tag, message, throwable)
        }
    }
    
    fun buffered(
        bufferSize: Int = 100,
        flushWriter: (List<LogEntry>) -> Unit
    ): (LogLevel, String, String, Throwable?) -> Unit {
        val buffer = mutableListOf<LogEntry>()
        
        return { level, tag, message, throwable ->
            // Note: This implementation is not thread-safe
            // For thread-safety, use platform-specific synchronization
            buffer.add(LogEntry(level, tag, message, throwable))
            if (buffer.size >= bufferSize) {
                flushWriter(buffer.toList())
                buffer.clear()
            }
        }
    }
}

data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable?
)