package com.michalkowol

import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.operation.Operation
import com.ninja_squad.dbsetup_kotlin.dbSetup
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import org.junit.rules.ExternalResource
import javax.sql.DataSource

class H2DatabaseResource : ExternalResource() {

    private lateinit var server: Server

    override fun before() {
        super.before()
        server = Server.createTcpServer()
    }

    override fun after() {
        super.after()
        server.shutdown()
    }
}

class DataSourceResource : ExternalResource() {

    private lateinit var _dataSource: HikariDataSource

    val dataSource: DataSource
        get() = _dataSource

    override fun before() {
        super.before()
        _dataSource = createLocalDataSource()
        cleanAndMigrateDatabase()
    }

    override fun after() {
        super.after()
        _dataSource.close()
    }

    private fun createLocalDataSource(): HikariDataSource {
        val config = ConfigFactory.load()
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = config.getString("datasource.jdbcUrl")
        hikariConfig.username = config.getString("datasource.username")
        hikariConfig.password = config.getString("datasource.password")
        return HikariDataSource(hikariConfig)
    }

    fun cleanAndMigrateDatabase() {
        val flyway = Flyway()
        flyway.dataSource = dataSource
        flyway.clean()
        flyway.migrate()
    }

    fun prepareDatabase(vararg operations: Operation) {
        dbSetup(dataSource) { execute(Operations.sequenceOf(operations.toList())) }.launch()
    }
}
