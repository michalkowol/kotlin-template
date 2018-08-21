package com.michalkowol.cars

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val carsModule = Kodein.Module("carsModule") {
    bind<CarsRepository>() with singleton { CarsRepository(instance()) }
    bind<CarsController>() with singleton { CarsController(instance(), instance()) }
}
