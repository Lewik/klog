package klog

import kotlin.reflect.KClass

actual object KLoggers {
    private val levels = HashMap<Regex, KLoggingLevels>()
    private val formatters = HashMap<Regex, Formatter>()
    val isDebug = Throwable().getStackTrace()[0].contains("Throwable")
    @Suppress("MemberVisibilityCanBePrivate")
    var defaultLoggingLevel: KLoggingLevels = if (isDebug) KLoggingLevels.DEBUG else  KLoggingLevels.INFO
    @Suppress("MemberVisibilityCanBePrivate")
    var defaultFormatter: Formatter = Formatter() {
        dateTime()
        text(" - ")
        level()
        text(":")
        if (isDebug) {
            text(" ")
            method(true)
        }
        message()
        throwable()
    }
    val writer: (String) -> Unit = ::println

    actual fun logger(owner: Any): KLogger = when (owner) {
        is String -> KLogger(NativeLogger(owner, calcLevel(owner), calcFormatter(owner), writer))
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

