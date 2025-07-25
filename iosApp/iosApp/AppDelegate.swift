//
//  AppDelegate.swift
//  iosApp
//
//  Created by MICHAEL NOGUERA GUZMAN on 25/07/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions
        launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        startKoin()

        let viewController = UIHostingController(rootView: WeatherScreen())

        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = viewController
        self.window?.makeKeyAndVisible()

        print("Weather App Started")
        return true
    }
}
