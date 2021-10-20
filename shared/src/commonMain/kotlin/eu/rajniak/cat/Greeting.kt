package eu.rajniak.cat

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}