package com.michalkowol.cars

import org.koin.dsl.module.module

val carsModule = module {
    single { CarsRepository(get()) }
    single { CarsController(get(), get()) }
}
