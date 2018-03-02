package com.michalkowol.cars

import com.softwareberg.Database
import com.softwareberg.params

class CarsRepository(private val database: Database) {

    fun findAll(): List<Car> {
        return database.findAll("SELECT id, name FROM cars") { row -> Car(row.int("id"), row.string("name")) }
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
        return database.findOne(sql) { row -> Car(row.int("id"), row.string("name")) }
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM cars WHERE id = :id".params("id" to id)
        val deletedCount = database.update(sql)
        return deletedCount
    }
}
