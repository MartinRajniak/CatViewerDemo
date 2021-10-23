package eu.rajniak.cat

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

// TODO: remove once https://github.com/Kotlin/kotlinx.coroutines/issues/1996 (1.6.0 hopefully)

expect fun runBlockingTest(block: suspend CoroutineScope.() -> Unit)
expect val testCoroutineContext: CoroutineContext
