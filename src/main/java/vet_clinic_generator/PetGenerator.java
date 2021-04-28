package vet_clinic_generator;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import static vet_clinic_generator.Utils.*;

public class PetGenerator {

    private final static String PETS_PATH = "src/data/pets.txt";
    private final static String MALE_PATH = "src/data/pet_name_male.txt";
    private final static String FEMALE_PATH = "src/data/pet_name_female.txt";

    public static void generatePet(int client_counter, Statement statement) {
            int amount_of_pets = getRand(1, 4);
            for (int j = 0; j < amount_of_pets; j++) {
                String animal_size = getRandomData(PETS_PATH);
                String[] f = animal_size.split(SPACE_SEPARATOR);
                String animal = f[0];
                String size = f[1];
                double weight = switch (size) {
                    case ("small") -> (double) getRand(1, 10) / 10;
                    case ("middle") -> getRand(1, 10);
                    case ("big") -> getRand(10, 30);
                    default -> getRand(1, 30);
                };
                String gender = "male";
                String name_data = MALE_PATH;
                if ((getRand(0, 2) == 1 || animal.equals("кошка")) && !animal.equals("кот") ) {
                        gender = "female";
                        name_data = FEMALE_PATH;
                }
                insertPet(statement, client_counter, animal, getRandomData(name_data), gender, getDate("for_pet"), weight);
            }
    }

    private static void insertPet(Statement statement, int client_counter, String animal, String name,
                                 String gender, Date date_of_birth, double weight) {
        try {
            statement.execute(String.format("INSERT INTO pet (client_id, animal, pet_name, pet_gender," +
                                " date_of_birth, weigh) VALUES ('%d','%s','%s','%s','%s','", client_counter, animal, name,
                        gender, date_of_birth) + weight + "')");
        } catch (SQLException throwable) {
            System.out.println(DB_CONNECTING_ERROR);
            throwable.printStackTrace();
        }
    }

}