package com.michalkowol.cars

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.softwareberg.Database
import com.softwareberg.Extractor
import org.junit.Test

class CarsServiceSpec {

    @Test
    fun `it should find all cars`() {
        // given
        val db = mock<Database> {
            on { findAll(any<String>(), any<Extractor<Car>>()) } doReturn listOf(Car(1, "Audi"), Car(2, "Ford"))
        }
        val carsRepository = CarsRepository(db)
        // when
        val cars = carsRepository.findAll()
        // then
        assertThat(cars).containsAllOf(Car(1, "Audi"), Car(2, "Ford")).inOrder()
    }
}
