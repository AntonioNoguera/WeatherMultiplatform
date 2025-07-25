package di

import org.koin.core.component.KoinComponent
import org.koin.dsl.module
import presentation.viewmodel.WeatherViewModel

actual val platformModule = module {
    single { WeatherViewModel(get()) }
}

@Suppress("unused")
object KotlinDependencies : KoinComponent {
    fun getWeatherViewModel() = getKoin().get<WeatherViewModel>()
}