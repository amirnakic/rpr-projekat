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

    public ObservableList<Employee> getEmployeesFromResultSet(ResultSet rs) {
        ObservableList<Department> departments = getDepartments();
        ObservableList<Employee> result = FXCollections.observableArrayList();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        Comparator<Employee> comparator = Comparator.comparingInt(Employee::getId);
        result.sort(comparator);
        return result;
    }

    public ObservableList<Employee> getEmployees() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FROM employee");
            ResultSet rs = statement.executeQuery();
            result = getEmployeesFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
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

    public boolean findDepartment(Department d) {
        ObservableList<Department> departments = getDepartments();
        for (Department department : departments)
            if (d.getId() == department.getId())
                return true;
        return false;
    }

    public void addDepartment(Department department) {
        if (findDepartment(department)) return;
        try {
            int id = getDepartments().size() + 1;
            start("INSERT OR REPLACE INTO department(id, name, current_number_of_employees, maximum_number_of_employees) VALUES(?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, department.getName());
            statement.setInt(3, department.getCurrentNumberOfEmployees());
            statement.setInt(4, department.getMaximumNumberOfEmployees());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void changeDepartment(Department department) {
        if (!findDepartment(department)) return;
        try {
            start("UPDATE department SET name = ?, current_number_of_employees = ?, maximum_number_of_employees = ? WHERE id = ?");
            statement.setString(1, department.getName());
            statement.setInt(2, department.getCurrentNumberOfEmployees());
            statement.setInt(3, department.getMaximumNumberOfEmployees());
            statement.setInt(4, department.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void removeDepartment(Department department) {
        if (!findDepartment(department)) return;
        try {
            start("DELETE FROM department WHERE id = ?");
            statement.setInt(1, department.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public boolean findEmployee(Employee e) {
        ObservableList<Employee> employees = getEmployees();
        for (Employee employee : employees)
            if (employee.getId() == e.getId())
                return true;
        return false;
    }

    public void addEmployee(Employee employee) {
        if (findEmployee(employee)) return;
        try {
            int id = getEmployees().size() + 1;
            start("INSERT OR REPLACE INTO employee(id, name, surname, phone_number, email_address, role, qualifications, work_experience, vacation_days_per_year, " +
                    "date_of_birth, date_of_employment, vacation, sick_leave, unpaid_leave, department) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getSurname());
            statement.setString(4, employee.getPhoneNumber());
            statement.setString(5, employee.getEmailAddress());
            statement.setString(6, employee.getRole());
            statement.setString(7, employee.getQualifications());
            statement.setInt(8, employee.getWorkExperience());
            statement.setInt(9, employee.getVacationDaysPerYear());
            statement.setDate(10, Date.valueOf(employee.getDateOfBirth()));
            statement.setDate(11, Date.valueOf(employee.getDateOfEmployment()));
            if (employee.isVacation())
                statement.setInt(12, 1);
            else statement.setInt(12, 0);
            if (employee.isSickLeave())
                statement.setInt(13, 1);
            else statement.setInt(13, 0);
            if (employee.isUnpaidLeave())
                statement.setInt(14, 1);
            else statement.setInt(14, 0);
            statement.setInt(15, employee.getDepartment().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void changeEmployee(Employee employee) {
        if (findEmployee(employee)) return;
        try {
            start("UPDATE employee SET name = ?, surname = ?, phone_number = ?, email_address = ?, role = ?, qualifications = ?," +
                    "work_experience = ?, vacation_days_per_year = ?, date_of_birth = ?, date_of_employment = ?, vacation = ?, sick_leave = ?," +
                    "unpaid_leave = ?, department = ? WHERE id = ?");
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getSurname());
            statement.setString(3, employee.getPhoneNumber());
            statement.setString(4, employee.getEmailAddress());
            statement.setString(5, employee.getRole());
            statement.setString(6, employee.getQualifications());
            statement.setInt(7, employee.getWorkExperience());
            statement.setInt(8, employee.getVacationDaysPerYear());
            statement.setDate(9, Date.valueOf(employee.getDateOfBirth()));
            statement.setDate(10, Date.valueOf(employee.getDateOfEmployment()));
            if (employee.isVacation())
                statement.setInt(11, 1);
            else statement.setInt(11, 0);
            if (employee.isSickLeave())
                statement.setInt(12, 1);
            else statement.setInt(12, 0);
            if (employee.isUnpaidLeave())
                statement.setInt(13, 1);
            else statement.setInt(13, 0);
            statement.setInt(14, employee.getDepartment().getId());
            statement.setInt(15, employee.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void removeEmployee(Employee employee) {
        if (!findEmployee(employee)) return;
        try {
            start("DELETE FROM employee WHERE id = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public ObservableList<Employee> getEmployeesFromDepartment(Department department) {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        if (!findDepartment(department)) return result;
        try {
            start("SELECT * FROM employee WHERE employee.department = ?");
            statement.setInt(1, department.getId());
            ResultSet rs = statement.executeQuery();
            result = getEmployeesFromResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public ObservableList<Employee> getEmployeesOnVacation() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FROM employee WHERE employee.vacation = ?");
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            result = getEmployeesFromResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnVacation(Vacation vacation) {
        if (!findEmployee(vacation.getEmployee())) return;
        if (vacation.getEmployee().isVacation()) return;
        vacation.getEmployee().setVacation(TRUE);
        changeEmployee(vacation.getEmployee());
        try {
            start("INSERT OR REPLACE INTO vacation(id, start_of_vacation, end_of_vacation, employee) VALUES(?, ?, null, ?)");
            statement.setInt(1, vacation.getId());
            statement.setDate(2, Date.valueOf(vacation.getStartOfVacation()));
            statement.setInt(4, vacation.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromVacation(Vacation vacation) {
        if (!findEmployee(vacation.getEmployee())) return;
        if (!vacation.getEmployee().isVacation()) return;
        vacation.getEmployee().setVacation(FALSE);
        changeEmployee(vacation.getEmployee());
        try {
            start("UPDATE vacation SET end_of_vacation = ? WHERE id = ?");
            statement.setDate(1, Date.valueOf(vacation.getEndOfVacation()));
            statement.setInt(2, vacation.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public ObservableList<Employee> getEmployeesOnSickLeave() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FROM employee WHERE employee.sick_leave = ?");
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            result = getEmployeesFromResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnSickLeave(SickLeave sickLeave) {
        if (!findEmployee(sickLeave.getEmployee())) return;
        if (sickLeave.getEmployee().isVacation()) return;
        sickLeave.getEmployee().setVacation(TRUE);
        changeEmployee(sickLeave.getEmployee());
        try {
            start("INSERT OR REPLACE INTO sick_leave(id, start_of_absence, end_of_absence, employee) VALUES(?, ?, null, ?)");
            statement.setInt(1, sickLeave.getId());
            statement.setDate(2, Date.valueOf(sickLeave.getStartOfAbsence()));
            statement.setInt(4, sickLeave.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromSickLeave(SickLeave sickLeave) {
        if (!findEmployee(sickLeave.getEmployee())) return;
        if (!sickLeave.getEmployee().isVacation()) return;
        sickLeave.getEmployee().setVacation(FALSE);
        changeEmployee(sickLeave.getEmployee());
        try {
            start("UPDATE sick_leave SET end_of_vacation = ? WHERE id = ?");
            statement.setDate(1, Date.valueOf(sickLeave.getEndOfAbsence()));
            statement.setInt(2, sickLeave.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public ObservableList<Employee> getEmployeesOnUnpaidLeave() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FROM employee WHERE employee.unpaid_leave = ?");
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            result = getEmployeesFromResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnUnpaidLeave(UnpaidLeave unpaidLeave) {
        if (!findEmployee(unpaidLeave.getEmployee())) return;
        if (unpaidLeave.getEmployee().isVacation()) return;
        unpaidLeave.getEmployee().setVacation(TRUE);
        changeEmployee(unpaidLeave.getEmployee());
        try {
            start("INSERT OR REPLACE INTO unpaid_leave(id, start_of_absence, end_of_absence, employee) VALUES(?, ?, null, ?)");
            statement.setInt(1, unpaidLeave.getId());
            statement.setDate(2, Date.valueOf(unpaidLeave.getStartOfAbsence()));
            statement.setInt(4, unpaidLeave.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromUnpaidLeave(UnpaidLeave unpaidLeave) {
        if (!findEmployee(unpaidLeave.getEmployee())) return;
        if (!unpaidLeave.getEmployee().isVacation()) return;
        unpaidLeave.getEmployee().setVacation(FALSE);
        changeEmployee(unpaidLeave.getEmployee());
        try {
            start("UPDATE unpaid_leave SET end_of_vacation = ? WHERE id = ?");
            statement.setDate(1, Date.valueOf(unpaidLeave.getEndOfAbsence()));
            statement.setInt(2, unpaidLeave.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }
}
