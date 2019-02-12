package ba.unsa.etf.rpr.projekat;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDAOTest {
    CompanyDAO dao = null;

    @Test
    void initDb() {
        if (dao != null) dao.close();

        File dbfile = new File("company.db");
        ClassLoader classLoader = getClass().getClassLoader();
        File srcfile = new File(classLoader.getResource("db/company.db").getFile());
        try {
            dbfile.delete();
            Files.copy(srcfile.toPath(), dbfile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Ne mogu kreirati bazu.");
        }

        dao = new CompanyDAO();
    }

    @Test
    void test1() {
        initDb();
        try {
            dao.addDepartment(new Department(1, 10, 15, "Automatics and Electronics"));
            dao.addDepartment(new Department(2, 8, 15, "Energetics"));
            dao.addDepartment(new Department(3, 15, 15, "Computer Science and Informatics"));
            dao.addDepartment(new Department(4, 6, 15, "Telecommunication"));
            dao.addDepartment(new Department(5, 8, 15, "Software Engineering"));
        } catch (DepartmentException de) {
            de.printStackTrace();
        }
        ObservableList<Department> departments = dao.getDepartments();
        assertEquals(5, departments.size());
        assertEquals("Automatics and Electronics", departments.get(0).getName());
        assertEquals(15, departments.get(1).getMaximumNumberOfEmployees());
        assertEquals(15, departments.get(2).getCurrentNumberOfEmployees());
        assertEquals(4, departments.get(3).getId());
        assertEquals("Software Engineering", departments.get(4).getName());
    }
}