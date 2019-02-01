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

    public ObservableList<Salary> getSalaries() {
        ObservableList<Salary> result = FXCollections.observableArrayList();
        ObservableList<Employee> employees = getEmployees();
        try {
            start("SELECT * FROM salary");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Salary s = new Salary();
                s.setId(rs.getInt(1));
                s.setBase(rs.getInt(2));
                s.setCoefficient(rs.getInt(3));
                s.setTaxes(rs.getInt(4));
                s.setContributions(rs.getInt(5));
                s.setMealAllowances(rs.getInt(6));
                int idOfEmployee = rs.getInt(7);
                for (Employee e : employees)
                    if (e.getId() == idOfEmployee) {
                        s.setEmployee(e);
                        break;
                    }
                result.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Salary> comparator = Comparator.comparingInt(Salary::getId);
        result.sort(comparator);
        close();
        return result;
    }

    public ObservableList<Vacation> getVacations() {
        ObservableList<Vacation> result = FXCollections.observableArrayList();
        ObservableList<Employee> employees = getEmployees();
        try {
            start("SELECT * FROM vacation");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Vacation v = new Vacation();
                v.setId(rs.getInt(1));
                v.setStartOfVacation(rs.getDate(2).toLocalDate());
                v.setEndOfVacation(rs.getDate(3).toLocalDate());
                int id0fEmployee = rs.getInt(4);
                for (Employee e : employees)
                    if (e.getId() == id0fEmployee) {
                        v.setEmployee(e);
                        break;
                    }
                result.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Vacation> comparator = Comparator.comparingInt(Vacation::getId);
        result.sort(comparator);
        close();
        return result;
    }

    public ObservableList<SickLeave> getSickLeaves() {
        ObservableList<SickLeave> result = FXCollections.observableArrayList();
        ObservableList<Employee> employees = getEmployees();
        try {
            start("SELECT * FROM sick_leave");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                SickLeave s = new SickLeave();
                s.setId(rs.getInt(1));
                s.setStartOfAbsence(rs.getDate(2).toLocalDate());
                s.setEndOfAbsence(rs.getDate(3).toLocalDate());
                int id0fEmployee = rs.getInt(4);
                for (Employee e : employees)
                    if (e.getId() == id0fEmployee) {
                        s.setEmployee(e);
                        break;
                    }
                result.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<SickLeave> comparator = Comparator.comparingInt(SickLeave::getId);
        result.sort(comparator);
        close();
        return result;
    }

    public ObservableList<UnpaidLeave> getUnpaidLeaves() {
        ObservableList<UnpaidLeave> result = FXCollections.observableArrayList();
        ObservableList<Employee> employees = getEmployees();
        try {
            start("SELECT * FROM unpaid_leave");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UnpaidLeave u = new UnpaidLeave();
                u.setId(rs.getInt(1));
                u.setStartOfAbsence(rs.getDate(2).toLocalDate());
                u.setEndOfAbsence(rs.getDate(3).toLocalDate());
                int id0fEmployee = rs.getInt(4);
                for (Employee e : employees)
                    if (e.getId() == id0fEmployee) {
                        u.setEmployee(e);
                        break;
                    }
                result.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<UnpaidLeave> comparator = Comparator.comparingInt(UnpaidLeave::getId);
        result.sort(comparator);
        close();
        return result;
    }
}
