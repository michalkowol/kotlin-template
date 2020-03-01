package com.michalkowol.cars

import com.softwareberg.Database
import com.softwareberg.params
import com.softwareberg.createExtractor

@Suppress("ExpressionBodySyntax")
class CarsRepository(private val database: Database) {

    private val idName = createExtractor { row -> Car(row.int("id"), row.string("name")) }

    fun findAll(): List<Car> {
        val sql = "SELECT id, name FROM cars"
        return database.findAll(sql, idName)
    }

    fun create(id: Int, name: String): Car {
        val sql = "INSERT INTO cars (id, name) VALUES (:id, :name)".params("id" to id, "name" to name)
        database.insert(sql)
        return Car(id, name)
    }

    fun changeName(id: Int, name: String): Int {
        val sql = "UPDATE cars SET name = :name WHERE id = :id".params("id" to id, "name" to name)
        val updatedCount = database.update(sql)
        return updatedCount
    }

    fun byId(id: Int): Car? {
        val sql = "SELECT id, name FROM cars WHERE id = :id".params("id" to id)
        return database.extractFirstRow(sql, idName)
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM cars WHERE id = :id".params("id" to id)
        val deletedCount = database.update(sql)
        return deletedCount
    }
}
