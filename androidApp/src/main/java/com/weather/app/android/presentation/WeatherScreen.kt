package com.weather.app.android.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.app.android.presentation.components.ErrorCard
import com.weather.app.android.presentation.components.ForecastList
import com.weather.app.android.presentation.components.WeatherCard
import presentation.weather.WeatherViewState
import presentation.weather.WeatherViewModel
import org.koin.compose.koinInject
import kotlinx.coroutines.launch
import presentation.core.ViewState

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.activate()
    }

    WeatherScreenContent(
        uiState = uiState,
        onSearchWeather = { cityName ->
            scope.launch {
                viewModel.fetchWeather(cityName)
            }

            scope.launch {
                viewModel.fetchForecast(cityName)
            }
        },
        onClearError = viewModel::clearWeatherError,
        onClearForecastError = viewModel::clearForecastError
    )
}

@Composable
fun WeatherScreenContent(
    uiState: WeatherViewState,
    onSearchWeather: (String) -> Unit = {},
    onClearError: () -> Unit = {},
    onClearForecastError: () -> Unit = {}
) {
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🌤️ Weather App",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Ciudad") },
            placeholder = { Text("Madrid, Barcelona...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchWeather(cityInput.trim()) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSearchWeather(cityInput.trim()) },
            enabled = cityInput.isNotBlank() && !uiState.isRefreshing,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Buscando...")
            } else {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val state = uiState.currentWeather) {
            is ViewState.Initial -> {
                Text(
                    text = "Ingresa el nombre de una ciudad",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is ViewState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Buscando información del clima...",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            is ViewState.Success -> {
                WeatherCard(weather = state.data)
            }

            is ViewState.Error -> {
                ErrorCard(error = state.error, onDismiss = onClearError)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val forecastState = uiState.forecast) {
            is ViewState.Initial -> {
                Text(
                    text = "Aquí va el pronóstico",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is ViewState.Loading -> {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando pronóstico...")
                }
            }

            is ViewState.Success -> {
                ForecastList(forecast = forecastState.data)
            }

            is ViewState.Error -> {
                ErrorCard(error = forecastState.error, onDismiss = onClearForecastError)
            }
        }
    }
}
