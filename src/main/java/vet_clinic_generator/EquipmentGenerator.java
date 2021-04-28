package vet_clinic_generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import static vet_clinic_generator.Utils.*;

public class EquipmentGenerator {

    private final static String EQUIPMENT_PATH = "src/data/equipment.txt";
    private final static String MANUFACTURER_PATH = "src/data/manufacturer.txt";

    public static void generate() {
        try {
            Files.readAllLines(Paths.get(EQUIPMENT_PATH)).forEach(str -> {
                String[] equipAndServ = str.split(SPACE_SEPARATOR + SPACE_SEPARATOR);
                String equipment = equipAndServ[0];
                String services = equipAndServ[1];
                String[] services_array = services.split(COMMA_SEPARATOR);
                for (String service: services_array) {
                    insertEquipment(service, equipment, getRandomData(MANUFACTURER_PATH),
                                getDate("for_write_off"), passwordAndSN(17, "ABCDEFGHIGKLMNOPQRSTVUWXYZ"));

                }
            });
        } catch (IOException e) {
            System.out.println(FILE_NOT_FOUND_ERROR);
            e.printStackTrace();
        }
    }

    private static void insertEquipment(String service, String equipment, String manufacturer, Date date_off_write, String pswd) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(String.format("INSERT INTO equipment (office_id, equipment_name," +
                            " manufacturer, write_off_date, serial_number) VALUES ('%s','%s','%s','%s','%s')",
                    service, equipment, manufacturer, date_off_write, pswd));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

}