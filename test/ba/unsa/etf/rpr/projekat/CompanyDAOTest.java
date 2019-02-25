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
    void departments() {
        initDb();
        Department d1, d2, d3, d4, d5 = null;
        try {
            d1 = new Department(1, 10, 15, "Automatics and Electronics");
            dao.addDepartment(d1);
            d2 = new Department(2, 8, 15, "Energetics");
            dao.addDepartment(d2);
            d3 = new Department(3, 15, 15, "Computer Science and Informatics");
            dao.addDepartment(d3);
            d4 = new Department(4, 6, 15, "Telecommunication");
            dao.addDepartment(d4);
            d5 = new Department(5, 8, 15, "Software Engineering");
            dao.addDepartment(d5);
        } catch (DepartmentException de) {
            de.printStackTrace();
        }

        //here we test methods addDepartment & getDepartments
        ObservableList<Department> departments = dao.getDepartments();
        assertEquals(5, departments.size());
        assertEquals("Automatics and Electronics", departments.get(0).getName());
        assertEquals(15, departments.get(1).getMaximumNumberOfEmployees());
        assertEquals(15, departments.get(2).getCurrentNumberOfEmployees());
        assertEquals(4, departments.get(3).getId());
        assertEquals("Software Engineering", departments.get(4).getName());

        //here we test method changeDepartment
        d5.setName("Changed");
        dao.changeDepartment(d5);
        departments = dao.getDepartments();
        assertEquals(5, departments.size());
        assertEquals("Changed", departments.get(4).getName());
        assertEquals("Computer Science and Informatics", departments.get(2).getName());

        //here we test method removeDepartment
        try {
            dao.removeDepartment(d5);
        } catch (DepartmentException e) {
            e.printStackTrace();
        }
        departments = dao.getDepartments();
        assertEquals(4, departments.size());
    }
}