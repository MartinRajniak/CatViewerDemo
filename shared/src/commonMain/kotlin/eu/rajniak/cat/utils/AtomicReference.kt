package eu.rajniak.cat.utils

expect class AtomicReference<V> constructor(initialValue: V) {
    fun set(value: V)
    fun get(): V
}
