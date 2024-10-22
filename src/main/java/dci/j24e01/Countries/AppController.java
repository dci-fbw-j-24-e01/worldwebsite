package dci.j24e01.Countries;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {
    Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/world",
            "root",
            "ABcd1234!!"
    );

    public AppController() throws SQLException {
    }

    @GetMapping("/")
    public String root(Model model) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT name FROM country");
        ResultSet resultSet = statement.executeQuery();

        List<String> countries = new ArrayList<>();

        while (resultSet.next()) {
            countries.add(resultSet.getString("name"));
        }

        model.addAttribute("countries", countries);
        return "index";
    }

    @GetMapping("/country")
    public String country(@RequestParam String name, Model model) throws SQLException {
        model.addAttribute("country", "Cities in " + name + ':');

        PreparedStatement statement = connection.prepareStatement("""
                SELECT city.name, district, city.population
                FROM city
                JOIN country ON Code = CountryCode
                WHERE country.name = ?""");

        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();
        List<City> cities = new ArrayList<>();

        while (resultSet.next()) {
            String cityName = resultSet.getString("name");
            cities.add(new City(
                    cityName,
                    resultSet.getString("district"),
                    resultSet.getLong("population"),
                    "https://en.wikipedia.org/wiki/" + cityName
            ));
        }

        model.addAttribute("cities", cities);
        return "country";
    }
}
