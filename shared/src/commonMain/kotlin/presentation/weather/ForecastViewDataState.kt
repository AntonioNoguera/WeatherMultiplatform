package presentation.weather

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import domain.forecast.models.ForecastModel
import presentation.core.ViewState

sealed class ForecastViewDataState {
    abstract val isLoading: Boolean

    data object Initial : ForecastViewDataState() {
        override val isLoading: Boolean = false
    }

    data class Loading @DefaultArgumentInterop.Enabled constructor(
        override val isLoading: Boolean = true
    ) : ForecastViewDataState()

    data class Success @DefaultArgumentInterop.Enabled constructor(
        val forecast: List<ForecastModel>,
        override val isLoading: Boolean = false
    ) : ForecastViewDataState()

    data class Error @DefaultArgumentInterop.Enabled constructor(
        val error: String,
        override val isLoading: Boolean = false
    ) : ForecastViewDataState()

    companion object {
        fun from(state: ViewState<List<ForecastModel>>): ForecastViewDataState = when (state) {
            is ViewState.Initial -> Initial
            is ViewState.Loading -> Loading()
            is ViewState.Success -> Success(state.data)
            is ViewState.Error -> Error(state.error)
        }
    }
}