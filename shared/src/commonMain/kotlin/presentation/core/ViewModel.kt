package presentation.core

//Implementación terriblemente sencilla, pero de momento es para no acomplejar lo que no ocupa ser complejo.

expect abstract class ViewModel() {
    protected open fun onCleared()
}