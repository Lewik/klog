package klog

interface WithLogging {
    val log: KLogger
}

class KLoggerHolder : WithLogging {
    override val log by lazy(LazyThreadSafetyMode.NONE) {
        KLoggers.logger(this)
    }
}

expect object KLoggers{
    fun logger(owner: Any): KLogger
}