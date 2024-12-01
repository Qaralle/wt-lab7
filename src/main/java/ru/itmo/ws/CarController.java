package ru.itmo.ws;

import ru.itmo.model.Car;
import ru.itmo.service.CarService;
import ru.itmo.service.CarServiceImpl;
import ru.itmo.service.exception.ArgumentException;
import ru.itmo.service.exception.DataException;
import ru.itmo.ws.dto.CarDto;
import ru.itmo.ws.dto.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/cars")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarController {
    private CarService carService = new CarServiceImpl();

    @GET
    public Response searchCars(@QueryParam("id") Long id,
                               @QueryParam("name") String name,
                               @QueryParam("price") Integer price,
                               @QueryParam("count") Integer count,
                               @QueryParam("power") Integer power) {
        try {
            List<Car> cars = carService.searchCars(id, name, price, count, power);
            return Response.ok(cars).build();
        } catch (ArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCar(@PathParam("id") Long id, CarDto carDto) {
        try {
            carService.updateCar(id, carDto.getName(), carDto.getPrice(),
                    carDto.getCount(), carDto.getPower());
            return Response.noContent().build();
        } catch (ArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (DataException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCar(@PathParam("id") Long id) {
        try {
            carService.deleteCar(id);
            return Response.noContent().build();
        } catch (ArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (DataException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createCar(CarDto carDto) {
        try {
            Long newCarId = carService.createCar(carDto.getName(),
                    carDto.getPrice(),
                    carDto.getCount(),
                    carDto.getPower());
            return Response.status(Response.Status.CREATED)
                    .entity(newCarId)
                    .build();
        } catch (ArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }
}