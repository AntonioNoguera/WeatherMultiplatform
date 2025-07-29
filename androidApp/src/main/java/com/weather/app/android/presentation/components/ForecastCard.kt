package com.weather.app.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.forecast.models.ForecastModel

@Composable
fun ForecastCard(weather: ForecastModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = weather.timeStamp, fontWeight = FontWeight.Bold)
                Text(text = weather.description.replaceFirstChar { it.uppercase() })
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${weather.temperature.toInt()}°C", fontWeight = FontWeight.Medium)
                Text(text = "💨 ${weather.humidity.toInt()} %")
            }
        }
    }
}
