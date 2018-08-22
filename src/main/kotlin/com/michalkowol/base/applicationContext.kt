package com.michalkowol.base

import com.michalkowol.cars.carsModule
import com.michalkowol.demo.demoModule
import com.michalkowol.hackernews.hackerNewsModule
import org.kodein.di.Kodein

val context = Kodein {
    import(configModule)
    import(errorsControllerModule)
    import(httpClientModule)
    import(httpServerModule)
    import(hackerNewsModule)
    import(demoModule)
    import(carsModule)
    import(jsonXmlModule)
    import(databaseModule)
}
