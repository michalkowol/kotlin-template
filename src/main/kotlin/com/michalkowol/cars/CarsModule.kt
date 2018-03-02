package com.michalkowol.cars

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.softwareberg.Database
import com.softwareberg.JsonMapper
import javax.inject.Singleton

class CarsModule : AbstractModule() {

    override fun configure() {}

    @Singleton
    @Provides
    private fun provideCarsRepository(database: Database): CarsRepository {
        return CarsRepository(database)
    }

    @Singleton
    @Provides
    private fun provideCarsController(carsRepository: CarsRepository, jsonMapper: JsonMapper): CarsController {
        return CarsController(carsRepository, jsonMapper)
    }
}
