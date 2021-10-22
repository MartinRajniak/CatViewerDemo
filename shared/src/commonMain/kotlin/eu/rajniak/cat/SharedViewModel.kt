package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.FakeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Suppress("EmptyDefaultConstructor")
expect open class SharedViewModel() {
    protected val sharedScope: CoroutineScope

    open fun onCleared()
}
