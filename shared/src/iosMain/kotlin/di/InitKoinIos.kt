package di

import org.koin.core.KoinApplication
import org.koin.dsl.module

fun initKoinIos(
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    module {
        single { doOnStartup }
    }
)