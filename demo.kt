import java.util.concurrent.*

abstract class ParametrizedDispatcherBase : CoroutineScope {
    abstract var dispatcher: String

    @Setup
    open fun setup() {
        coroutineContext = when {
            dispatcher == "fjp" -> ForkJoinPool.commonPool().asCoroutineDispatcher()
            dispatcher == "scheduler" -> {
                Dispatchers.Default
            }
            dispatcher.startsWith("ftp") -> {
                newFixedThreadPoolContext(dispatcher.substring(4).toInt(), dispatcher).also { closeable = it }
            }
            else -> error("Unexpected dispatcher: $dispatcher")
        }
    }
}