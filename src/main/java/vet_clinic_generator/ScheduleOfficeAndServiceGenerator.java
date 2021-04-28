package vet_clinic_generator;

import java.sql.SQLException;
import java.sql.Statement;
import static vet_clinic_generator.Utils.*;

public class ScheduleOfficeAndServiceGenerator {

    public static void insertScheduleOfficeOrService(Statement statement,Integer schedule_of_visit_id,
                                                     String id, String table_name, String args) {
        try {
            statement.execute(String.format("INSERT INTO " + table_name + " " + args +
                    " VALUES ('%s','%s')", schedule_of_visit_id, id));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

}
