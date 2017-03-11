package com.michalkowol.cars

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.softwareberg.Database
import com.softwareberg.Extractor
import org.junit.Test

class CarsServiceSpec {

    @Test
    fun itShouldFindAllCars() {
        // given
        val db = mock<Database> {
            on { findAll(any<String>(), any<Extractor<Car>>()) } doReturn listOf(Car(1, "Audi"), Car(2, "Ford"))
        }
        val carsRepository = CarsRepository(db)
        // when
        val cars = carsRepository.findAll()
        // then
        assertThat(cars, hasSize(equalTo(2)))
        assertThat(cars[0], equalTo(Car(1, "Audi")))
    }
}
