package eu.rajniak.cat.utils

import kotlin.native.concurrent.AtomicInt

actual class AtomicInt actual constructor(initialValue: Int) {
    private val atom = AtomicInt(initialValue)

    actual fun get(): Int = atom.value
    actual fun addAndGet(delta: Int): Int = atom.addAndGet(delta)
}
