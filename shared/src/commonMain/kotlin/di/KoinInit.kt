package di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import domain.useCases.GetWeatherUseCase
import data.remote.WeatherAPI
import data.respositories.WeatherRepositoryImpl
import domain.respositories.WeatherRepository


fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(
            appModule,
            platformModule,
            coreModule,
        )
    }

    // Lógica de inicialización simplificada
    val koin = koinApplication.koin
    val doOnStartup = koin.get<() -> Unit>()
    doOnStartup.invoke()

    return koinApplication
}

private val coreModule = module {
    // Network
    single { WeatherAPI(get()) }

    // Repository
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }

    // Use Cases
    single { GetWeatherUseCase(get()) }
}

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T = get(parameters = { parametersOf(*params) })
