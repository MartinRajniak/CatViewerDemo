package eu.rajniak.cat.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.native.internal.GC

@Suppress("EmptyDefaultConstructor")
actual open class SharedViewModel actual constructor() {
    protected actual val sharedScope: CoroutineScope = createViewModelScope()

    actual open fun onCleared() {
        sharedScope.cancel()

        dispatch_async(dispatch_get_main_queue()) { GC.collect() }
    }
}

@ThreadLocal
private var createViewModelScope: () -> CoroutineScope = {
    CoroutineScope(createUIDispatcher())
}

private fun createUIDispatcher(): CoroutineDispatcher = UIDispatcher()
