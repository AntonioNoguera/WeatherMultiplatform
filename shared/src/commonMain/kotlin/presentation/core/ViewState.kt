package presentation.core

sealed class ViewState<out T> {
    abstract val isLoading: Boolean

    data object Initial : ViewState<Nothing>() {
        override val isLoading: Boolean = false
    }

    data class Loading(
        override val isLoading: Boolean = true
    ) : ViewState<Nothing>()

    data class Success<out T>(
        val data: T,
        override val isLoading: Boolean = false
    ) : ViewState<T>()

    data class Error(
        val error: String,
        override val isLoading: Boolean = false
    ) : ViewState<Nothing>()
}
