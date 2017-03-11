package com.michalkowol.cars

import com.michalkowol.BadRequestException
import com.michalkowol.NotFoundException
import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.*
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarsController @Inject constructor(private val carsRepository: CarsRepository, private val jsonMapper: JsonMapper) {

    fun start() {
        get("/cars", this::cars, jsonMapper::write)
        post("/cars", this::createCar, jsonMapper::write)
        get("/cars/create", this::createCarWithQueryParams, jsonMapper::write)
        get("/cars/:id", this::carById, jsonMapper::write)
        put("/cars/:id", this::changeCarName, jsonMapper::write)
        delete("/cars/:id", this::deleteCar, jsonMapper::write)
    }

    private fun cars(request: Request, response: Response): List<Car> {
        response.type("application/json")
        val cars = carsRepository.findAll()
        return cars
    }

    private fun carById(request: Request, response: Response): Car {
        response.type("application/json")
        val id = request.params("id").toIntOrNull() ?: throw BadRequestException("id must be integer")
        return carsRepository.byId(id) ?: throw NotFoundException("Car with id=$id not found")
    }

    private fun createCar(request: Request, response: Response): Car {
        response.type("application/json")
        response.status(HTTP_CREATED)
        val (id, name) = jsonMapper.read<Car>(request.body())
        return carsRepository.create(id, name)
    }

    private fun createCarWithQueryParams(request: Request, response: Response): Car {
        // not RESTfull - only for demo
        response.type("application/json")
        response.status(HTTP_CREATED)
        val id = request.queryParams("id")?.toIntOrNull() ?: throw BadRequestException("Missing 'id' query param or 'id' is not integer")
        val name = request.queryParams("name") ?: throw BadRequestException("Missing 'name' query param")
        return carsRepository.create(id, name)
    }

    private fun changeCarName(request: Request, response: Response): Car {
        response.type("application/json")
        val idFromParam = Integer.parseInt(request.params("id"))
        val (id, name) = jsonMapper.read<Car>(request.body())
        if (id != idFromParam) {
            throw BadRequestException("Request id=$idFromParam is not equal Car.id=$id")
        }
        carsRepository.changeName(id, name)
        return carsRepository.byId(id) ?: throw NotFoundException("Car with id=$id not found")
    }

    private fun deleteCar(request: Request, response: Response): String {
        response.type("application/json")
        response.status(HTTP_NO_CONTENT)
        val id = Integer.parseInt(request.params("id"))
        carsRepository.delete(id)
        return ""
    }
}
