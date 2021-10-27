package eu.rajniak.cat.utils

expect class AtomicInt(initialValue: Int) {
    fun get(): Int
    fun addAndGet(delta: Int): Int
}
