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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import domain.models.Weather
import presentation.viewmodel.WeatherViewState
import presentation.viewmodel.WeatherViewModel
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = koinInject()
    val uiState by viewModel.weatherState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // Activar el ViewModel cuando se monte el composable
    LaunchedEffect(viewModel) {
        viewModel.activate()
    }

    WeatherScreenContent(
        uiState = uiState,
        onSearchWeather = { cityName ->
            scope.launch {
                viewModel.searchWeather(cityName)
            }
        },
        onClearError = viewModel::clearError
    )
}

@Composable
fun WeatherScreenContent(
    uiState: WeatherViewState,
    onSearchWeather: (String) -> Unit = {},
    onClearError: () -> Unit = {}
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
            enabled = cityInput.isNotBlank() && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
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

        // Manejo de estados con sealed classes
        when (uiState) {
            is WeatherViewState.Initial -> {
                Text(
                    text = "Ingresa el nombre de una ciudad",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is WeatherViewState.Loading -> {
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

            is WeatherViewState.Success -> {
                WeatherCard(weather = uiState.weather)
            }

            is WeatherViewState.Error -> {
                ErrorCard(
                    error = uiState.error,
                    onDismiss = onClearError
                )
            }
        }
    }
}

@Composable
fun ErrorCard(error: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "❌ Error",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                TextButton(onClick = onDismiss) {
                    Text("✕")
                }
            }
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun WeatherCard(weather: Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.cityName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weather.temperature.toInt()}°C",
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = weather.description.replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetail(
                    icon = "💧",
                    label = "Humedad",
                    value = "${weather.humidity}%"
                )
                WeatherDetail(
                    icon = "💨",
                    label = "Viento",
                    value = "${weather.windSpeed.toInt()} km/h"
                )
            }
        }
    }
}

@Composable
fun WeatherDetail(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
