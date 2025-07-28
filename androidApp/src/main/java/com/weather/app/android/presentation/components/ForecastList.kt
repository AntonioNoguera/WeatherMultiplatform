package com.weather.app.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.weather.models.Weather

@Composable
fun ForecastList(forecast: List<Weather>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "🔮 Pronóstico extendido",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        forecast.forEachIndexed { index, weather ->
            ForecastCard(weather = weather, day = "Día ${index + 1}")
        }
    }
}
