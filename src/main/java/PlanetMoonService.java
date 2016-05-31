import java.sql.*;
import java.util.ArrayList;

public class PlanetMoonService {

        private final Connection connection;

        public PlanetMoonService(Connection connection) {
            this.connection = connection;
        }

        //Write a method createTables which takes the database connection.
        //Run it right after you create your connection. It should execute two things:
        //Calls DROP TABLE IF EXISTS people
        //Calls CREATE TABLE people
        //(id IDENTITY, first_name VARCHAR, last_name VARCHAR, email VARCHAR, country VARCHAR, ip VARCHAR)

        public void createTables() throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS planets");
            statement.execute("CREATE TABLE planets (id IDENTITY, name VARCHAR, radius INT , supportsLife BOOLEAN, distanceFromSun DOUBLE)");
            statement.execute("DROP TABLE IF EXISTS moons");
            statement.execute("CREATE TABLE moons (id IDENTITY, planetID INT, name VARCHAR, color VARCHAR)");
        }

        //Write a method called insertPerson which takes the database connection and the columns.
        public void insertPlanets(Connection connection, Planet p) throws SQLException {
            // insert the new person
            PreparedStatement statement = connection.prepareStatement("INSERT INTO planets VALUES (NULL, ?, ?, ?, ?)");
            statement.setString(1, p.name);
            statement.setInt(2, p.radius);
            statement.setBoolean(3, p.supportsLife);
            statement.setDouble(4, p.distanceFromSun);

            statement.executeUpdate();

            // get the generated id
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next(); // read the first line of results

            // set the generated id into my person
            p.id = resultSet.getString(1);

            for(Moon m : p.moons){
                statement = connection.prepareStatement("INSERT INTO moons VALUES (NULL, ?, ?, ?)");
                statement.setString(1, p.id);
                statement.setString(2, m.name);
                statement.setString(3, m.color);


                statement.executeUpdate();

                // get the generated id
                resultSet = statement.getGeneratedKeys();
                resultSet.next(); // read the first line of results

                // set the generated id into my person
                m.id = resultSet.getString(1);
            }
        }
        //Write a method called selectPerson which takes the database connection and an id and returns a Person
        public Planet selectPlanet(Connection connection, Integer id) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM planets WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            Planet planet = new Planet();

            planet.id = (Integer.toString(resultSet.getInt(1)));
            planet.name = (resultSet.getString(2));
            planet.radius = (resultSet.getInt(3));
            planet.supportsLife = (resultSet.getBoolean(4));
            planet.distanceFromSun = (resultSet.getDouble(5));

            preparedStatement = connection.prepareStatement("SELECT * FROM moons WHERE planetId = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Moon moon = new Moon();
                moon.id = (resultSet.getString(1));
                moon.name = (resultSet.getString(2));
                moon.color = (resultSet.getString(3));

                planet.moons.add(moon);
            }

            return planet;
        }


        public ArrayList<Planet> selectPlanets(Connection connection) throws SQLException {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM planets;");

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Planet>planets = new ArrayList<>();

            while (resultSet.next()) {

                Planet planet = new Planet();

                planet.id = (Integer.toString(resultSet.getInt(1)));
                planet.name = (resultSet.getString(2));
                planet.radius = (resultSet.getInt(3));
                planet.supportsLife = (resultSet.getBoolean(4));
                planet.distanceFromSun = (resultSet.getDouble(5));

                planets.add(planet);

                preparedStatement = connection.prepareStatement("SELECT * FROM moons WHERE planetId = ?");
                preparedStatement.setString(1, planet.id);
                ResultSet resultSet2 = preparedStatement.executeQuery();

                while(resultSet2.next()){
                    Moon moon = new Moon();
                    moon.id = (resultSet2.getString(1));
                    moon.name = (resultSet2.getString(2));
                    moon.color = (resultSet2.getString(3));

                    planet.moons.add(moon);
                }

            }

            return planets;
        }

    }


