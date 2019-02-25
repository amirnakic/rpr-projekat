package ba.unsa.etf.rpr.projekat;

import javafx.collections.ObservableList;
import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static java.lang.Boolean.FALSE;
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

    @Test
    void employees() {
        initDb();
        Department d1 = null;
        Employee e1, e2 = null;
        try {
            d1 = new Department(1, 0, 15, "Automatics and Electronics");
            dao.addDepartment(d1);
            e1 = new Employee(1, 10, 40, "Meho", "Mehic", "033555444",
                    "mehomehic@gmail.com", "professor", "doctor", LocalDate.of(1973, 1, 1),
                    LocalDate.of(2015, 1, 1), FALSE, FALSE, FALSE, d1);
            e2 = new Employee(2, 0, 40, "Test", "Testovic", "033555444",
                    "testtestovic@gmail.com", "professor", "doctor", LocalDate.of(1988, 1, 1),
                    LocalDate.now(), FALSE, FALSE, FALSE, d1);
            dao.addEmployee(e1);
            dao.addEmployee(e2);
        } catch (DepartmentException de) {
            de.printStackTrace();
        } catch (EmployeeException e) {
            e.printStackTrace();
        }

        //here we test methods getEmployees, getDepartments & addEmployee
        ObservableList<Employee> employees = dao.getEmployees();
        ObservableList<Department> departments = dao.getDepartments();
        assertEquals(2, employees.size());
        assertEquals("Meho", employees.get(0).getName());
        assertEquals(2, departments.get(0).getCurrentNumberOfEmployees());

        //here we test method changeEmployee
        e2.setName("Amir");
        dao.changeEmployee(e2);
        departments = dao.getDepartments();
        employees = dao.getEmployees();
        assertEquals(2, employees.size());
        assertEquals("Amir", employees.get(1).getName());

        //here we test method removeEmployee
        try {
            dao.removeEmployee(e2);
        } catch (EmployeeException e) {
            e.printStackTrace();
        }
        employees = dao.getEmployees();
        assertEquals(1, employees.size());
        assertEquals("Mehic", employees.get(0).getSurname());

        //here we test method getEmployeesFromDepartment
        try {
            employees = dao.getEmployeesFromDepartment(d1);
        } catch (DepartmentException e) {
            e.printStackTrace();
        }
        assertEquals(1, employees.size());
        assertEquals("mehomehic@gmail.com", employees.get(0).getEmailAddress());
    }

    @Test
    void vacation() {
        initDb();
        Department d1 = new Department(1, 0, 15, "Automatics and Electronics");
        Employee e1 = new Employee(1, 10, 40, "Meho", "Mehic", "033555444",
                "mehomehic@gmail.com", "professor", "doctor", LocalDate.of(1973, 1, 1),
                LocalDate.of(2015, 1, 1), FALSE, FALSE, FALSE, d1);
        Vacation v1 = new Vacation(1, LocalDate.of(2019, 1, 1), LocalDate.of(2019, 2, 1), e1);
        try {
            dao.addDepartment(d1);
            dao.addEmployee(e1);
            dao.sendEmployeeOnVacation(v1);
        } catch (DepartmentException de) {
            de.printStackTrace();
        } catch (EmployeeException e) {
            e.printStackTrace();
        } catch (VacationException e) {
            e.printStackTrace();
        }

        //here we test methods related with class Vacation
        ObservableList<Employee> employees = dao.getEmployeesOnVacation();
        assertEquals(1, employees.size());
        try {
            dao.getEmployeeBackFromVacation(v1);
        } catch (VacationException e) {
            e.printStackTrace();
        }
        employees = dao.getEmployeesOnVacation();
        assertEquals(0, employees.size());
    }

    @Test
    void sick_leave() {
        initDb();
        Department d1 = new Department(1, 0, 15, "Automatics and Electronics");
        Employee e1 = new Employee(1, 10, 40, "Meho", "Mehic", "033555444",
                "mehomehic@gmail.com", "professor", "doctor", LocalDate.of(1973, 1, 1),
                LocalDate.of(2015, 1, 1), FALSE, FALSE, FALSE, d1);
        SickLeave sl = new SickLeave(1, LocalDate.of(2019, 1, 1), LocalDate.of(2019, 1, 12), e1);
        try {
            dao.addDepartment(d1);
            dao.addEmployee(e1);
            dao.sendEmployeeOnSickLeave(sl);
        } catch (DepartmentException de) {
            de.printStackTrace();
        } catch (EmployeeException e) {
            e.printStackTrace();
        } catch (SickLeaveException e) {
            e.printStackTrace();
        }

        //here we test methods related with class SickLeave
        ObservableList<Employee> employees = dao.getEmployeesOnSickLeave();
        assertEquals(1, employees.size());
        try {
            dao.getEmployeeBackFromSickLeave(sl);
        } catch (SickLeaveException e) {
            e.printStackTrace();
        }
        employees = dao.getEmployeesOnSickLeave();
        assertEquals(0, employees.size());
    }

    @Test
    void unpaid_leave() {
        initDb();
        Department d1 = new Department(1, 0, 15, "Automatics and Electronics");
        Employee e1 = new Employee(1, 10, 40, "Meho", "Mehic", "033555444",
                "mehomehic@gmail.com", "professor", "doctor", LocalDate.of(1973, 1, 1),
                LocalDate.of(2015, 1, 1), FALSE, FALSE, FALSE, d1);
        UnpaidLeave ul = new UnpaidLeave(1, LocalDate.of(2019, 1, 1), LocalDate.of(2019, 1, 12), e1);
        try {
            dao.addDepartment(d1);
            dao.addEmployee(e1);
            dao.sendEmployeeOnUnpaidLeave(ul);
        } catch (DepartmentException de) {
            de.printStackTrace();
        } catch (EmployeeException e) {
            e.printStackTrace();
        } catch (UnpaidLeaveException e) {
            e.printStackTrace();
        }

        //here we test methods related with class SickLeave
        ObservableList<Employee> employees = dao.getEmployeesOnUnpaidLeave();
        assertEquals(1, employees.size());
        try {
            dao.getEmployeeBackFromUnpaidLeave(ul);
        } catch (UnpaidLeaveException e) {
            e.printStackTrace();
        }
        employees = dao.getEmployeesOnSickLeave();
        assertEquals(0, employees.size());
    }

}