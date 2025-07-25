package di

import org.koin.core.component.KoinComponent

import org.koin.core.module.Module
import org.koin.dsl.module
import presentation.viewmodel.WeatherViewModel
import io.ktor.client.engine.darwin.Darwin

actual val platformModule: Module = module {
    // Cliente HTTP específico para iOS
    single { Darwin.create() }

    // ViewModel
    single { WeatherViewModel(get()) }
}

@Suppress("unused")
object KotlinDependencies : KoinComponent {
    fun getWeatherViewModel(): WeatherViewModel = getKoin().get()
}