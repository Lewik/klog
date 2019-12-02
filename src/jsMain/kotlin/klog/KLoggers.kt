package klog


actual object KLoggers {
    private val loggers = js("({})")
    private val levels = mutableListOf<Pair<Regex, KLoggingLevels>>()

    @Suppress("MemberVisibilityCanBePrivate")
    var defaultLoggingLevel: KLoggingLevels = KLoggingLevels.INFO

    actual fun logger(owner: Any) = when (owner) {
        is String -> internalLogger(owner)
        else -> internalLogger(owner)
    }

    private fun internalLogger(owner: Any): KLogger {
        val d = owner.asDynamic()
        @Suppress("UnsafeCastFromDynamic")
        return d.__logger ?: run {
            val logger = logger(owner::class.simpleName ?: owner::class.js.name)
            d.__logger = logger
            logger
        }
    }

    @Suppress("UnsafeCastFromDynamic")
    private fun internalLogger(name: String): KLogger = loggers[name] ?: run {
        val logger = KLogger(JSLogger(name, calcLevel(name)))
        loggers[name] = logger
        logger
    }

    @Suppress("unused")
    fun loggingLevel(regex: Regex, level: KLoggingLevels) {
        levels.add(regex to level)
    }

    private fun calcLevel(name: String) =
        levels
            .filter { it.first.matches(name) }
            .maxBy { it.second }
            ?.second
            ?: defaultLoggingLevel
}
