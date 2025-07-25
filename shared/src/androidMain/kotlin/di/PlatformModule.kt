package di

import domain.useCases.GetWeatherUseCase
import org.koin.dsl.module
import presentation.viewmodel.WeatherViewModel

actual val platformModule = module {
    // Aquí van dependencias específicas de Android
    // Por ahora está vacío
}