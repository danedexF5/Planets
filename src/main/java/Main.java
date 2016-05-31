import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {

        //Create an H2 database to hold planets and moons.
        Server server = Server.createTcpServer("-baseDir", "./data").start();
        // create our connection
        String jdbcUrl = "jdbc:h2: " + server.getURL() + "/main";
        System.out.println("jdbc url: " + jdbcUrl);
        Connection connection = DriverManager.getConnection("jdbc:h2:./data/main");

        PlanetMoonService planetMoonService = new PlanetMoonService(connection);
        planetMoonService.createTables();

        Spark.get(
                "/planets",
                (request, response) -> {
                    /*Implement the get /planets endpoint.
                    This endpoint must query from the database of planets and moons
                    and return an ArrayList of all planets in JSON format.*/
                    //return an arraylist of all planets

                    ArrayList<Planet>planets = planetMoonService.selectPlanets(connection);

                    Gson gson = new GsonBuilder().create();
                    return gson.toJson(planets);

    }

    );


    Spark.get(
            "/planet",
            (request,response)->

    {
        //Implement the get /planet endpoint. This endpoint must retrieve a single planet from your
        //database based on an id provided in the url. IE: http://localhost:4567/planet?id=5.
        Planet planet;

        String planetId = request.queryParams("id");
        planet = planetMoonService.selectPlanet(connection, Integer.parseInt(planetId));

        /*Planet planet = new Planet();
        planet.name = request.params(":name");
        planet.distanceFromSun = 1;
        planet.radius = 6387; //km
        planet.supportsLife = true;

        Moon moon = new Moon();
        moon.name = "Luna";
        moon.color = "white";

        planet.moons.add(moon);*/

        Gson gson = new GsonBuilder().create();
        return gson.toJson(planet);
    }

    );
    Spark.post(
            "/planet",
            (request,response)->

    {
        //Implement the post /planet endpoint. This endpoint must receive a planet in JSON form, parse it
        //into an instance of Planet, and then write it into your database, moons and all. The new planet
        //will appear in subsequent calls to /planets and /planet.
        String planetJson = request.queryParams("planet");
        Gson gson = new GsonBuilder().create();
        Planet planet = gson.fromJson(planetJson, Planet.class);
        planetMoonService.insertPlanets(connection, planet);

        return "";
    }

    );

}
}
