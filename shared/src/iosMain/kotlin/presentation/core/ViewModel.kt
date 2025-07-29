package presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual abstract class ViewModel actual constructor() {
    private val job = SupervisorJob()
    protected actual val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default + job)

    protected actual open fun onCleared() {
        job.cancel()
    }
}
