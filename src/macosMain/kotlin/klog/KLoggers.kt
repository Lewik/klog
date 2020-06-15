package klog

import platform.Foundation.NSLog
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.reflect.KClass

actual object KLoggers {
    val isDebug = Throwable().getStackTrace()[0].contains("Throwable")

    private val levels = HashMap<Regex, KLoggingLevels>()
    private val formatters = HashMap<Regex, Formatter>()
    private val defaultLoggingLevelAtomic
            = AtomicReference(if (isDebug) KLoggingLevels.DEBUG else  KLoggingLevels.INFO)

    //    case with println
    private val defaultFormatterAtomic =
        AtomicReference(
            Formatter {
                dateTime()
                text(" - ")
                level()
                text(":")
                if (isDebug) {
                    text(" ")
                    method(true)
                }
                text(" ")
                message()
                throwable()
            }.freeze()
        )
    private fun log(line: String) { println(line) }

//    case with NSLog
//    private val defaultFormatterAtomic =
//        AtomicReference(
//            Formatter {
//                level()
//                text(":")
//                if (isDebug) {
//                    text(" ")
//                    method(true)
//                }
//                text(" ")
//                message()
//                throwable()
//            }.freeze()
//        )
//    private fun log(line: String) { NSLog(line) }

    private val writerAtomic: AtomicReference<(String) -> Any> =
        AtomicReference(::log.freeze())

    var defaultLoggingLevel: KLoggingLevels
        get() = defaultLoggingLevelAtomic.value
        set(value) { defaultLoggingLevelAtomic.value = value }

    var defaultFormatter: Formatter
        get() = defaultFormatterAtomic.value
        set(value) { defaultFormatterAtomic.value = value.freeze() }

    var writer: (String) -> Any
        get() = writerAtomic.value
        set(value) { writerAtomic.value = value.freeze() }

    actual fun logger(owner: Any): KLogger = when (owner) {
        is String -> KLogger(NativeLogger(calcLevel(owner), calcFormatter(owner), writer))
        is KClass<*> -> logger(name(owner::class))
        else -> logger(name(owner::class))
    }

    fun name(forClass: KClass<out Any>): String {
        val name = forClass.qualifiedName ?: forClass.simpleName ?: "<object>"
        val slicedName = when {
            name.contains("Kt$") -> name.substringBefore("Kt$")
            name.contains("$") -> name.substringBefore("$")
            else -> name
        }
        return when {
            slicedName.endsWith(".") -> slicedName.substring(0, slicedName.length - 1)
            else -> slicedName
        }
    }

    fun loggingLevel(regex: Regex, level: KLoggingLevels) {
        levels.put(regex, level)
    }

    private fun calcLevel(name: String) =
        levels
            .filter { it.key.matches(name) }
            .maxBy { it.value }
            ?.value
            ?: defaultLoggingLevel

    private fun calcFormatter(name: String) =
        formatters
            .filter { it.key.matches(name) }
            .maxBy { it.value.toString() }
            ?.value
            ?: defaultFormatter
}
