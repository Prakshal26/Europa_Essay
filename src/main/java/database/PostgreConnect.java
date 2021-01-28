package database;

import pojo.CountryList;
import pojo.Reference;
import pojo.Region;

import java.sql.*;

public class PostgreConnect {

    static Connection connection;

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/europa_essay",
                            "postgres", "root");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }
        return connection;
    }

    public static int insertCountryList(CountryList countryList) {

        int id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT into country (name,xml_id,alt_heading)" + "VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            statement.setString(i++, countryList.getName());
            statement.setString(i++, countryList.getXmlId());
            statement.setString(i++, countryList.getAltHeading());

            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next()){
                id=rs.getInt(1);
            }
            statement.close();
            return id;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return id;
    }

    public static int insertRegion(Region region) {

        int id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT into regionData (name,alt_heading, description, country_id,xml_id)" + "VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            statement.setString(i++, region.getName());
            statement.setString(i++, region.getAltHeading());
            statement.setString(i++, region.getDescription());
            statement.setInt(i++, region.getCountryId());
            statement.setString(i++, region.getXmlId());
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next()){
                id=rs.getInt(1);
            }
            statement.close();
            return id;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return id;
    }
    public static int insertReference(Reference reference) {

        int id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT into reference (heading,alt_heading,description, parent_id, data_id,xml_id)" + "VALUES (?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            statement.setString(i++, reference.getHeading());
            statement.setString(i++, reference.getAltHeading());
            statement.setString(i++, reference.getDescription());
            statement.setInt(i++, reference.getParentId());
            statement.setInt(i++, reference.getDataId());
            statement.setString(i++, reference.getXmlId());
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next()){
                id=rs.getInt(1);
            }
            statement.close();
            return id;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return id;
    }
}
