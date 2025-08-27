package klog

enum class LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;
    
    override fun toString(): String = name
}