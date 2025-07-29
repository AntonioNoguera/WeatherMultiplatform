package com.weather.app.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.forecast.models.ForecastModel

@Composable
fun ForecastList(forecast: List<ForecastModel>) {

    val scrollState = rememberScrollState()

    Text(
        text = "🔮 Pronóstico extendido",
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Column(modifier = Modifier.verticalScroll(scrollState).padding(bottom = 16.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)) {

        forecast.forEach { forecast ->
            ForecastCard(weather = forecast)
        }
    }
}
