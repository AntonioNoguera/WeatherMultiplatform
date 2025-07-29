package presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import androidx.lifecycle.ViewModel


actual abstract class ViewModel actual constructor() : ViewModel() {
    private val job = SupervisorJob()
    protected actual val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    actual override fun onCleared() {
        job.cancel()
    }
}