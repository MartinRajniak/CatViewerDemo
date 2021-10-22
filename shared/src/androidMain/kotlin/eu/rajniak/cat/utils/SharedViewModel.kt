package eu.rajniak.cat.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class SharedViewModel actual constructor() : ViewModel() {
    protected actual val sharedScope: CoroutineScope = viewModelScope

    public actual override fun onCleared() {
        super.onCleared()
    }
}
