package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.FakeData
import eu.rajniak.cat.utils.CommonFlow
import eu.rajniak.cat.utils.SharedViewModel
import eu.rajniak.cat.utils.asCommonFlow
import kotlinx.coroutines.flow.MutableStateFlow

class CatsViewModel : SharedViewModel() {
    private val _cats = MutableStateFlow(FakeData.cats)
    val cats: CommonFlow<List<Cat>> = _cats.asCommonFlow()
}
