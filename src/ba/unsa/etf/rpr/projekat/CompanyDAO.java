package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Comparator;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CompanyDAO {
    private Connection conn;
    private PreparedStatement statement;

    public CompanyDAO() {
    }

    public void prepareStatement(String s) {
        try {
            statement = conn.prepareStatement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start(String s) {
        try {
            String url = "jdbc:sqllite:database.db";
            conn = DriverManager.getConnection(url);
            prepareStatement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Department> getDepartments() {
        ObservableList<Department> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FORM department");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Department d = new Department();
                d.setId(rs.getInt(1));
                d.setCurrentNumberOfEmployees(rs.getInt(3));
                d.setMaximumNumberOfEmployees(rs.getInt(4));
                d.setName(rs.getString(2));
                result.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Department> comparator = Comparator.comparingInt(Department::getId);
        result.sort(comparator);
        close();
        return result;
    }

    public ObservableList<Employee> getEmployees() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        ObservableList<Department> departments = getDepartments();
        try {
            start("SELECT * FROM employee");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt(1));
                e.setName(rs.getString(2));
                e.setSurname(rs.getString(3));
                e.setPhoneNumber(rs.getString(4));
                e.setEmailAddress(rs.getString(5));
                e.setRole(rs.getString(6));
                e.setQualifications(rs.getString(7));
                e.setWorkExperience(rs.getInt(8));
                e.setVacationDaysPerYear(rs.getInt(9));
                e.setDateOfBirth(rs.getDate(10).toLocalDate());
                e.setDateOfEmployment(rs.getDate(11).toLocalDate());
                if (rs.getInt(12) == 1)
                    e.setVacation(TRUE);
                else e.setVacation(FALSE);
                if (rs.getInt(13) == 1)
                    e.setSickLeave(TRUE);
                else e.setSickLeave(FALSE);
                if (rs.getInt(14) == 1)
                    e.setUnpaidLeave(TRUE);
                else e.setUnpaidLeave(FALSE);
                int idOfDepartment = rs.getInt(15);
                for (Department d : departments)
                    if (d.getId() == idOfDepartment) {
                        e.setDepartment(d);
                        break;
                    }
                result.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Employee> comparator = Comparator.comparingInt(Employee::getId);
        result.sort(comparator);
        close();
        return result;
    }
}
