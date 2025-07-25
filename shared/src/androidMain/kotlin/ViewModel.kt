package presentation
import androidx.lifecycle.ViewModel as AndroidXViewModel

actual abstract class ViewModel actual constructor() : AndroidXViewModel() {
    actual override fun onCleared() {
        super.onCleared()
    }
}
