package eu.rajniak.cat.utils

import kotlin.native.concurrent.AtomicReference

actual class AtomicReference<V> actual constructor(initialValue: V) {
    private val atom = AtomicReference(initialValue)

    actual fun set(value: V) {
        atom.value = value
    }
    actual fun get(): V = atom.value
}
