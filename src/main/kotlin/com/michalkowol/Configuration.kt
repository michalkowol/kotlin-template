package com.michalkowol

import com.google.inject.Guice
import com.google.inject.Injector
import com.michalkowol.cars.CarsModule
import com.michalkowol.hackernews.HackerNewsModule

object Configuration {

    val injector = createInjector()

    private fun createInjector(): Injector {
        return Guice.createInjector(
            ConfigModule(),
            ErrorsControllerModule(),
            HttpClientModule(),
            HttpServerModule(),
            HackerNewsModule(),
            CarsModule(),
            JsonXmlModule(),
            DatabaseModule()
        )
    }

}
