package presentation.core

import kotlinx.coroutines.CoroutineScope

//Implementación terriblemente sencilla, pero de momento es para no acomplejar lo que no ocupa ser complejo.

expect abstract class ViewModel() {
    protected val viewModelScope: CoroutineScope
    protected open fun onCleared()
}