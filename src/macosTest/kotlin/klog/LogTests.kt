package klog

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleLog: WithLogging by KLoggerHolder() {
    fun test() {
        log.trace { "Trace logging" }
        log.debug { "Debug logging" }
        log.info { "Info logging" }
        log.warn { "Warn logging" }
        log.error { "Error logging" }
    }
}

class LogTests {

    private val logResult = AtomicReference<List<String>>(listOf())

    private fun addLine(line: String) {
        val list = ArrayList(logResult.value)
        list.add(line)
        logResult.value = list.freeze()
    }

    init {
        KLoggers.writer = ::addLine
    }

    @Test
    fun testLevel() {
        logResult.value = listOf()
        SimpleLog().test()
        assertEquals(4, logResult.value.size)
        checkLogString("DEBUG: k.SimpleLog.test(11) Debug logging", logResult.value[0])
        checkLogString("INFO: k.SimpleLog.test(12) Info logging", logResult.value[1])
        checkLogString("WARN: k.SimpleLog.test(13) Warn logging", logResult.value[2])
        checkLogString("ERROR: k.SimpleLog.test(14) Error logging", logResult.value[3])
    }

    fun checkLogString(expected: String, actual: String, message: String? = null) {
        assertEquals('.', actual[4])
        assertEquals('.', actual[7])
        assertEquals('-', actual[10])
        assertEquals(':', actual[13])
        assertEquals(':', actual[16])
        assertEquals(' ', actual[19])
        assertEquals('-', actual[20])
        assertEquals(' ', actual[21])
        assertEquals(expected, actual.substring(22), message)
    }
}

