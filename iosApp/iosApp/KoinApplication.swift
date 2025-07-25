//
//  KoinApplication.swift
//  iosApp
//
//  Created by MICHAEL NOGUERA GUZMAN on 25/07/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation
import shared

func startKoin() {
    let doOnStartup = { NSLog("Hello from iOS/Swift!") }
    
    let koinApplication = InitKoinIosKt.doInitKoinIos(
        doOnStartup: doOnStartup
    )
    
    
    _koin = koinApplication.koin
}


private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    return _koin!
}
