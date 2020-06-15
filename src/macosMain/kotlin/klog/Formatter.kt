package klog

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.posix.gmtime
import platform.posix.time
import platform.posix.time_tVar
import platform.posix.tm

class Formatter() {
    companion object {
        private const val loggerMainClass = "kfun:mu.internal.KLoggerNative"
    }

    constructor(body: Formatter.() -> Unit): this() {
        body.invoke(this)
    }

    private val formatters: MutableList<(KLoggingLevels, Throwable?, msg: Any?) -> String> = ArrayList()

    fun formatMessage(
        level: KLoggingLevels,
        t: Throwable?,
        msg: Any?
    ): String {
        return formatters.joinToString(separator = "") { it.invoke(level, t, msg) }
    }

    fun level() {
        formatters.add{ value, _, _ -> value.name}
    }

    fun dateTime() {
        formatters.add{ _, _, _ ->
            getDateStruct()?.let {
                "${year(it)}.${month(it)}${day(it)}-${hour(it)}:${minute(it)}:${second(it)} (${tz(it)})"
            } ?:""
        }
    }

    private fun year(t: tm) = (t.tm_year + 1900).toString()
    private fun month(t: tm) = (t.tm_mon + 1).toString().let { if (it.length > 1) it else "0$it" }
    private fun day(t: tm) = t.tm_mday.toString()
    private fun hour(t: tm) = t.tm_hour.toString()
    private fun minute(t: tm) = t.tm_min.toString().let { if (it.length > 1) it else "0$it" }
    private fun second(t: tm) = t.tm_sec.toString().let { if (it.length > 1) it else "0$it" }
    private fun tz(t: tm) = t.tm_zone?.pointed?.value.toString()

    private fun getDateStruct() = memScoped {
        val t = alloc<time_tVar>()
        t.value = time(null)
        val local = gmtime(t.ptr)?.pointed
        local
    }

    fun message() {
        formatters.add{ _, _, value -> value.toString()}
    }

    fun text(value: String) {
        formatters.add{ _, _, _ -> value}
    }

    fun method(withLine: Boolean = false) {
        formatters.add { _, _, _ ->
            val stackLine = getRealMethod(Throwable().getStackTrace())
            val funLine = if (stackLine.contains("kfun:")) {
                stackLine.substringAfter("kfun:")
            } else {
                stackLine
            }
            val lineNumber = if (withLine) {
                val parts = stackLine.split(":")
                if (parts.size > 2) {
                    "(${parts[parts.size-2]})"
                } else {
                    "(?)"
                }
            } else {
                null
            }
            var methodName = if (funLine.contains("(")) {
                funLine.substringBefore("(")
            } else {
                funLine
            }
            methodName = if (methodName.contains("#")) {
                methodName.substringBefore("#")
            } else {
                methodName
            }
            if (lineNumber != null) {
                "$methodName$lineNumber"
            } else {
                methodName
            }
        }
    }

    private fun getRealMethod(strings: Array<String>): String {
        var kotlinNativeArrived = false
        for (string in strings) {
          if (kotlinNativeArrived) {
              if (!string.contains(loggerMainClass)) {
                  return string
              }
          } else {
              kotlinNativeArrived = string.contains(loggerMainClass)
          }
        }
        return ""
    }

    fun throwable(prefix: String = "\n  ", separator: String = "\n, ") {
        formatters.add { _, value, _ ->
            if (value != null) {
                var msg = "${prefix}${throwableLabel(value)}"
                var previous = value
                var current = previous.cause
                while (current != null && previous != current) {
                    msg += "${separator}Caused by: ${throwableLabel(current)}"
                    previous = current
                    current = previous.cause
                }
                msg
            } else {
                ""
            }
        }
    }

    fun throwableLabel(current: Throwable): String {
        val className = current::class.qualifiedName ?: current::class.simpleName ?: "<object>"
        return "$className: ${current.message}"
    }

}
