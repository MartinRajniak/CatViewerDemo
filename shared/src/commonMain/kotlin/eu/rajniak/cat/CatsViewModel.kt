package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.FakeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CatsViewModel: SharedViewModel() {
    private val _cats = MutableStateFlow(FakeData.cats)
    val cats: StateFlow<List<Cat>> = _cats
}