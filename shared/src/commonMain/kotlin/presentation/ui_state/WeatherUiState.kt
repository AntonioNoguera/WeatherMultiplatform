package presentation.ui_state

import domain.models.Weather

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)