package com.michalkowol.cars

import com.despegar.sparkjava.test.SparkServer
import com.michalkowol.DataSourceResource
import com.michalkowol.H2DatabaseResource
import com.ninja_squad.dbsetup.Operations.deleteAllFrom
import com.ninja_squad.dbsetup_kotlin.insertInto
import com.softwareberg.Database
import com.softwareberg.JsonMapper
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.StringStartsWith.startsWith
import org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.RuleChain
import spark.servlet.SparkApplication

class CarsControllerIntegrationSpec {

    class TestSparkApplication : SparkApplication {
        override fun init() {
            CarsController(CarsRepository(Database(dataSourceResource.dataSource)), JsonMapper.create()).start()
        }
    }

    companion object {
        private val h2DatabaseResource = H2DatabaseResource()

        private val dataSourceResource = DataSourceResource()

        private val testServer = SparkServer(TestSparkApplication::class.java, 4567)

        @JvmField
        @ClassRule
        val rules = RuleChain
            .outerRule(h2DatabaseResource)
            .around(dataSourceResource)
            .around(testServer)

        @JvmStatic
        @BeforeClass
        fun setup() {
            dataSourceResource.cleanAndMigrateDatabase()
        }
    }

    private val deleteAllCars = deleteAllFrom("cars")

    private val insertCars = insertInto("cars") {
        columns("id", "name")
        values(1, "Audi")
        values(2, "VW")
    }

    @Test
    fun `it should find all cars with rest`() {
        // given
        dataSourceResource.prepareDatabase(deleteAllCars, insertCars)
        val getAllCars = testServer.get("/cars", false)

        // when
        val httpResponse = testServer.execute(getAllCars)
        val json = httpResponse.body().toString(Charsets.UTF_8)

        // then
        assertThat(httpResponse.code(), equalTo(200))
        assertThat(json, startsWith("[{\"id\":1"))
        assertThat(json, containsString("VW"))
    }
}
