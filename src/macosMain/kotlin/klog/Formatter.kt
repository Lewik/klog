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
        private const val loggerMainClass = "kfun:klog.KLogger"
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
                "${year(it)}.${month(it)}.${day(it)}-${hour(it)}:${minute(it)}:${second(it)}"
            } ?:""
        }
    }

    private fun year(t: tm) = (t.tm_year + 1900).toString()
    private fun month(t: tm) = (t.tm_mon + 1).toString().let { if (it.length > 1) it else "0$it" }
    private fun day(t: tm) = t.tm_mday.toString()
    private fun hour(t: tm) = t.tm_hour.toString().let { if (it.length > 1) it else "0$it" }
    private fun minute(t: tm) = t.tm_min.toString().let { if (it.length > 1) it else "0$it" }
    private fun second(t: tm) = t.tm_sec.toString().let { if (it.length > 1) it else "0$it" }

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

    fun method(withLine: Boolean = false, compactClassName: Boolean = true) {
        formatters.add { _, _, _ ->
            val stackLine = getMethodBeforeLogger(Throwable().getStackTrace())
            val funLine = if (stackLine.contains("kfun:")) {
                stackLine.substringAfter("kfun:")
            } else {
                stackLine
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
            methodName = if (methodName.contains("$")) {
                methodName.substringBefore("$")
            } else {
                methodName
            }
            if (compactClassName) {
                val methodParts = methodName.split(".")
                val partsCount = methodParts.size
                if (partsCount > 2) {
                    val b = StringBuilder()
                    for (i in 0 until partsCount - 2) {
                        b.append(methodParts[i][0]).append('.')
                    }
                    b.append(methodParts[partsCount - 2]).append('.').append(methodParts[partsCount - 1])
                    methodName = b.toString()
                }
            }
            if (withLine) {
                val parts = stackLine.split(":")
                if (parts.size > 2) {
                    "$methodName(${parts[parts.size-2]})"
                } else {
                    "$methodName(?)"
                }
            } else {
                methodName
            }
        }
    }

    private fun getMethodBeforeLogger(strings: Array<String>): String {
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

    private fun throwableLabel(current: Throwable): String {
        val className = current::class.qualifiedName ?: current::class.simpleName ?: "<object>"
        return "$className: ${current.message}"
    }

}
