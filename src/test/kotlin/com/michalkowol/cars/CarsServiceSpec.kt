package com.michalkowol.cars

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.softwareberg.Database
import com.softwareberg.Extractor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
        assertThat(cars).containsExactly(Car(1, "Audi"), Car(2, "Ford"))
    }
}
