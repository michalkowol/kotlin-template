package com.michalkowol.cars

import com.google.common.truth.Truth.assertThat
import com.michalkowol.DataSourceResource
import com.michalkowol.H2DatabaseResource
import com.ninja_squad.dbsetup_kotlin.dbSetup
import com.softwareberg.Database
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.RuleChain

class CarsServiceIntegrationSpec {

    companion object {
        private val h2DatabaseResource = H2DatabaseResource()

        private val dataSourceResource = DataSourceResource()

        @JvmField
        @ClassRule
        val rules = RuleChain
            .outerRule(h2DatabaseResource)
            .around(dataSourceResource)

        @JvmStatic
        @BeforeClass
        fun setup() {
            dataSourceResource.cleanAndMigrateDatabase()
        }
    }

    @Test
    fun `it should find all cars`() {
        // given
        dbSetup(dataSourceResource.dataSource) {
            deleteAllFrom("cars")
            insertInto("cars") {
                columns("id", "name")
                values(1, "Audi")
                values(2, "Opel")
                values(3, "BMW")
            }
        }.launch()
        val carsRepository = CarsRepository(Database(dataSourceResource.dataSource))
        // when
        val cars = carsRepository.findAll()
        // then
        assertThat(cars).containsExactly(Car(1, "Audi"), Car(2, "Opel"), Car(3, "BMW")).inOrder()
    }
}
