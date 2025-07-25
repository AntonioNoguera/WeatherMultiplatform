package di

import domain.useCases.GetWeatherUseCase
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.engine.okhttp.OkHttp

import presentation.viewmodel.WeatherViewModel

actual val platformModule: Module = module {
    // HTTP Engine para Android
    single { OkHttp.create() }

    // ViewModel
    single { WeatherViewModel(get()) }
}