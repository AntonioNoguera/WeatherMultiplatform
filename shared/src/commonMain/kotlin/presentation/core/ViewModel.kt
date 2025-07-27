package presentation.core

expect abstract class ViewModel() {
    protected open fun onCleared()
}