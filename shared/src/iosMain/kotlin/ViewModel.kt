package presentation

actual abstract class ViewModel actual constructor() {
    protected actual open fun onCleared() {}

    fun clear() {
        onCleared()
    }
}