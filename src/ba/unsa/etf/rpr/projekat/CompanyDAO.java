package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.temporal.ChronoUnit.DAYS;

public class CompanyDAO {
    private Connection conn = null;
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
            String url = "jdbc:sqlite:company.db";
            conn = DriverManager.getConnection(url);
            PreparedStatement testStatement = conn.prepareStatement("SELECT * FROM department, salary, employee, sick_leave, unpaid_leave, vacation");
            testStatement.executeQuery();
        } catch (SQLException e) {
            generateDatabase();
        }
        prepareStatement(s);
    }

    private void generateDatabase() {
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream("company.db.sql"));
            String sqlStatement = "";
            while (input.hasNext()) {
                sqlStatement += input.nextLine();
                if (sqlStatement.charAt(sqlStatement.length() - 1) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlStatement);
                        sqlStatement = "";
                    } catch (SQLException e) {
                        conn.close();
                        e.printStackTrace();
                    }
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            start("SELECT * FROM department");
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
        ObservableList<Department> departments = getDepartments();
        ObservableList<Employee> result = FXCollections.observableArrayList();
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
                s.setDate(rs.getDate(7).toLocalDate());
                int idOfEmployee = rs.getInt(8);
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

    public int availableIDForDepartments(ObservableList<Department> departments) {
        int result = 1;
        for (Department d : departments) {
            if (d.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public void addDepartment(Department department) throws DepartmentException {
        if (findDepartment(department))
            throw new DepartmentException("Department " + department.getName() + " already exists.");
        try {
            int id = availableIDForDepartments(getDepartments());
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

    public void removeDepartment(Department department) throws DepartmentException {
        if (!findDepartment(department))
            throw new DepartmentException("Department " + department.getName() + " doesn't exist.");
        ObservableList<Employee> employees = getEmployees();
        boolean isEmpty = true;
        for (Employee e : employees) {
            if (e.getDepartment().getId() == department.getId()) {
                isEmpty = false;
            }
        }
        if (!isEmpty) throw new DepartmentException("Department " + department.getName() + " isn't empty.");
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

    public boolean isDepartmentAvailableForNewEmployees(Department department) {
        ObservableList<Department> departments = getDepartments();
        for (Department d : departments) {
            if (d.getId() == department.getId())
                if (d.getCurrentNumberOfEmployees() + 1 > d.getMaximumNumberOfEmployees())
                    return false;
        }
        return true;
    }

    public int availableIDForEmployees(ObservableList<Employee> employees) {
        int result = 1;
        for (Employee e : employees) {
            if (e.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public int availableIDForVacations(ObservableList<Vacation> vacations) {
        int result = 1;
        for (Vacation v : vacations) {
            if (v.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public int availableIDForSickLeaves(ObservableList<SickLeave> sickLeaves) {
        int result = 1;
        for (SickLeave sl : sickLeaves) {
            if (sl.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public int availableIDForUnpaidLeaves(ObservableList<UnpaidLeave> unpaidLeaves) {
        int result = 1;
        for (UnpaidLeave ul : unpaidLeaves) {
            if (ul.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public int availableIDForSalaries(ObservableList<Salary> salaries) {
        int result = 1;
        for (Salary s : salaries) {
            if (s.getId() == result) {
                result++;
            } else break;
        }
        return result;
    }

    public void addEmployee(Employee employee) throws EmployeeException {
        if (!isDepartmentAvailableForNewEmployees(employee.getDepartment()))
            throw new EmployeeException("Employee " + employee.toString() + " can't be registered because department " + employee.getName() + " is already full.");
        employee.getDepartment().setCurrentNumberOfEmployees(employee.getDepartment().getCurrentNumberOfEmployees() + 1);
        changeDepartment(employee.getDepartment());
        try {
            int id = availableIDForEmployees(getEmployees());
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

    public void removeEmployee(Employee employee) throws EmployeeException {
        if (!findEmployee(employee))
            throw new EmployeeException("Employee " + employee.toString() + " isn't registered yet.");
        employee.getDepartment().setCurrentNumberOfEmployees(employee.getDepartment().getCurrentNumberOfEmployees() - 1);
        changeDepartment(employee.getDepartment());
        try {
            start("DELETE FROM employee WHERE id = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
            close();
            start("DELETE FROM vacation WHERE employee = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
            close();
            start("DELETE FROM sick_leave WHERE employee = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
            close();
            start("DELETE FROM unpaid_leave WHERE employee = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
            close();
            start("DELETE FROM salary WHERE employee = ?");
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
            close();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
    }

    public ObservableList<Employee> getEmployeesFromDepartment(Department department) throws DepartmentException {
        ObservableList<Department> departments = getDepartments();
        ObservableList<Employee> result = FXCollections.observableArrayList();
        if (!findDepartment(department))
            throw new DepartmentException("Department " + department.getName() + " doesn't exist.");
        try {
            start("SELECT * FROM employee WHERE employee.department = ?");
            statement.setInt(1, department.getId());
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
        ObservableList<Department> departments = getDepartments();
        try {
            start("SELECT * FROM employee WHERE employee.vacation = ?");
            statement.setInt(1, 1);
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
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public long numberOfUsedVacationDaysThisYear(Employee employee) {
        long result = 0;
        try {
            start("SELECT * FROM vacation WHERE vacation.employee = ?");
            statement.setInt(1, employee.getId());
            ResultSet rs = statement.executeQuery();
            List<Vacation> vacationsThisYear = new ArrayList<>();
            int currentYear = LocalDate.now().getYear();
            while (rs.next()) {
                LocalDate start = rs.getDate(2).toLocalDate(), end = rs.getDate(3).toLocalDate();
                if (start.getYear() == currentYear || end.getYear() == currentYear) {
                    Vacation v = new Vacation();
                    v.setId(rs.getInt(1));
                    v.setStartOfVacation(start);
                    v.setEndOfVacation(end);
                    v.setEmployee(employee);
                    vacationsThisYear.add(v);
                }
            }
            for (Vacation v : vacationsThisYear) {
                if (v.getStartOfVacation().getYear() != currentYear)
                    result += DAYS.between(LocalDate.of(currentYear, 1, 1), v.getEndOfVacation());
                else if (v.getEndOfVacation().getYear() != currentYear)
                    result += DAYS.between(v.getStartOfVacation(), LocalDate.of(currentYear, 12, 31));
                else
                    result += DAYS.between(v.getStartOfVacation(), v.getEndOfVacation());
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnVacation(Vacation vacation) throws VacationException {
        if (!findEmployee(vacation.getEmployee())) return;
        if (vacation.getEmployee().isVacation())
            throw new VacationException("Employee " + vacation.getEmployee().toString() + " is already on vacation.");
        if (vacation.getEmployee().isSickLeave())
            throw new VacationException("Employee " + vacation.getEmployee().toString() + " can't be sent on vacation because he's currently on sick leave.");
        if (vacation.getEmployee().isUnpaidLeave())
            throw new VacationException("Employee " + vacation.getEmployee().toString() + " can't be sent on vacation because he's currently on unpaid leave.");
        int currentYear = LocalDate.now().getYear();
        LocalDate start = vacation.getStartOfVacation(), end = vacation.getEndOfVacation();
        long daysBetween1 = 0, daysBetween2 = 0;
        if (start.getYear() != currentYear && end.getYear() == currentYear)
            daysBetween1 = DAYS.between(LocalDate.of(currentYear, 1, 1), end);
        else if (start.getYear() == currentYear && end.getYear() != currentYear) {
            daysBetween1 = DAYS.between(start, LocalDate.of(currentYear, 12, 31));
            daysBetween2 = DAYS.between(LocalDate.of(currentYear + 1, 1, 1), end);
        } else daysBetween1 = DAYS.between(start, end);
        if (daysBetween1 + numberOfUsedVacationDaysThisYear(vacation.getEmployee()) > vacation.getEmployee().getVacationDaysPerYear() || daysBetween2 > vacation.getEmployee().getVacationDaysPerYear())
            throw new VacationException("Number of days in this vacation overflow allowed number of vacation days per year for this employee.");
        vacation.getEmployee().setVacation(TRUE);
        changeEmployee(vacation.getEmployee());
        try {
            int id = availableIDForVacations(getVacations());
            start("INSERT OR REPLACE INTO vacation(id, start_of_vacation, end_of_vacation, employee) VALUES(?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(vacation.getStartOfVacation()));
            statement.setDate(3, Date.valueOf(vacation.getEndOfVacation()));
            statement.setInt(4, vacation.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromVacation(Employee employee) throws VacationException {
        if (!findEmployee(employee)) return;
        if (!employee.isVacation())
            throw new VacationException("Employee " + employee.toString() + " isn't currently on vacation.");
        employee.setVacation(FALSE);
        changeEmployee(employee);
    }

    public ObservableList<Employee> getEmployeesOnSickLeave() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        ObservableList<Department> departments = getDepartments();
        try {
            start("SELECT * FROM employee WHERE employee.sick_leave = ?");
            statement.setInt(1, 1);
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
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnSickLeave(SickLeave sickLeave) throws SickLeaveException {
        if (!findEmployee(sickLeave.getEmployee())) return;
        if (sickLeave.getEmployee().isVacation())
            throw new SickLeaveException("Employee " + sickLeave.getEmployee().toString() + " can't be sent on sick leave because he's currently on vacation.");
        if (sickLeave.getEmployee().isUnpaidLeave())
            throw new SickLeaveException("Employee " + sickLeave.getEmployee().toString() + " can't be sent on sick leave because he's currently on unpaid leave.");
        if (sickLeave.getEmployee().isSickLeave())
            throw new SickLeaveException("Employee " + sickLeave.getEmployee().toString() + " is already on sick leave.");
        sickLeave.getEmployee().setSickLeave(TRUE);
        changeEmployee(sickLeave.getEmployee());
        try {
            int id = availableIDForSickLeaves(getSickLeaves());
            start("INSERT OR REPLACE INTO sick_leave(id, start_of_absence, end_of_absence, employee) VALUES(?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(sickLeave.getStartOfAbsence()));
            statement.setDate(3, Date.valueOf(sickLeave.getEndOfAbsence()));
            statement.setInt(4, sickLeave.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromSickLeave(Employee employee) throws SickLeaveException {
        if (!findEmployee(employee)) return;
        if (!employee.isSickLeave())
            throw new SickLeaveException("Employee " + employee.toString() + " isn't currently on sick leave.");
        employee.setSickLeave(FALSE);
        changeEmployee(employee);
    }

    public ObservableList<Employee> getEmployeesOnUnpaidLeave() {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        ObservableList<Department> departments = getDepartments();
        try {
            start("SELECT * FROM employee WHERE employee.unpaid_leave = ?");
            statement.setInt(1, 1);
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
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        close();
        return result;
    }

    public void sendEmployeeOnUnpaidLeave(UnpaidLeave unpaidLeave) throws UnpaidLeaveException {
        if (!findEmployee(unpaidLeave.getEmployee())) return;
        if (unpaidLeave.getEmployee().isVacation())
            throw new UnpaidLeaveException("Employee " + unpaidLeave.getEmployee().toString() + " can't be sent on unpaid leave because he's currently on vacation.");
        if (unpaidLeave.getEmployee().isSickLeave())
            throw new UnpaidLeaveException("Employee " + unpaidLeave.getEmployee().toString() + " can't be sent on unpaid leave because he's currently on sick leave.");
        if (unpaidLeave.getEmployee().isUnpaidLeave())
            throw new UnpaidLeaveException("Employee " + unpaidLeave.getEmployee().toString() + " is already on unpaid leave.");
        unpaidLeave.getEmployee().setUnpaidLeave(TRUE);
        changeEmployee(unpaidLeave.getEmployee());
        try {
            int id = availableIDForUnpaidLeaves(getUnpaidLeaves());
            start("INSERT OR REPLACE INTO unpaid_leave(id, start_of_absence, end_of_absence, employee) VALUES(?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(unpaidLeave.getStartOfAbsence()));
            statement.setDate(3, Date.valueOf(unpaidLeave.getEndOfAbsence()));
            statement.setInt(4, unpaidLeave.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return;
        }
        close();
    }

    public void getEmployeeBackFromUnpaidLeave(Employee employee) throws UnpaidLeaveException {
        if (!findEmployee(employee)) return;
        if (!employee.isUnpaidLeave())
            throw new UnpaidLeaveException("Employee " + employee.toString() + " isn't currently on unpaid leave.");
        employee.setUnpaidLeave(FALSE);
        changeEmployee(employee);
    }

    public void removeSalaryBecauseOfUnpaidLeave(Employee employee) {
        int id = getIDOfLastSalaryForEmployee(employee);
        try {
            start("DELETE FROM salary WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
        close();
    }

    public void addSalary(Salary s) {
        try {
            int id = availableIDForSalaries(getSalaries());
            start("INSERT OR REPLACE INTO salary(id, base, coefficient, taxes, contributions, meal_allowances, date, employee) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setInt(2, s.getBase());
            statement.setInt(3, s.getCoefficient());
            statement.setInt(4, s.getTaxes());
            statement.setInt(5, s.getContributions());
            statement.setInt(6, s.getMealAllowances());
            statement.setDate(7, Date.valueOf(s.getDate()));
            statement.setInt(8, s.getEmployee().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
        close();
    }

    public void updateSalary(Salary s) {
        int id = getIDOfLastSalaryForEmployee(s.getEmployee());
        try {
            start("UPDATE salary SET base = ?, coefficient = ?, taxes = ?, contributions = ?, meal_allowances = ? WHERE id = ?");
            statement.setInt(1, s.getBase());
            statement.setInt(2, s.getCoefficient());
            statement.setInt(3, s.getTaxes());
            statement.setInt(4, s.getContributions());
            statement.setInt(5, s.getMealAllowances());
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
        close();
    }

    public ObservableList<Salary> getAllSalariesForEmployee(Employee employee) {
        ObservableList<Salary> result = FXCollections.observableArrayList();
        if (!findEmployee(employee)) return result;
        try {
            start("SELECT * FROM salary WHERE salary.employee = ?");
            statement.setInt(1, employee.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Salary s = new Salary();
                s.setId(rs.getInt(1));
                s.setBase(rs.getInt(2));
                s.setCoefficient(rs.getInt(3));
                s.setTaxes(rs.getInt(4));
                s.setContributions(rs.getInt(5));
                s.setMealAllowances(rs.getInt(6));
                s.setDate(rs.getDate(7).toLocalDate());
                s.setEmployee(employee);
                result.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Salary> comparator = Comparator.comparingInt(Salary::getId);
        result.sort(comparator);
        close();
        return result;
    }

    public ObservableList<Salary> getAllSalariesInOneYear(int year) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getSalaries();
        for (Salary s : salaries) {
            if (s.getDate().getYear() == year)
                result.add(s);
        }
        return result;
    }

    public ObservableList<Salary> getAllSalariesInOneMonth(int month, int year) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getAllSalariesInOneYear(year);
        for (Salary s : salaries) {
            if (s.getDate().getMonthValue() == month)
                result.add(s);
        }
        return result;
    }

    public ObservableList<Salary> getAllSalariesForEmployeeInOneYear(int year, Employee employee) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getAllSalariesForEmployee(employee);
        for (Salary s : salaries) {
            if (s.getDate().getYear() == year)
                result.add(s);
        }
        return result;
    }

    public Salary getSpecificSalary(int month, int year, Employee employee) {
        ObservableList<Salary> salaries = getAllSalariesForEmployeeInOneYear(year, employee);
        for (Salary s : salaries) {
            if (s.getDate().getMonthValue() == month)
                return s;
        }
        return null;
    }

    public ObservableList<Salary> getAllSalariesFromDepartment(Department d) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getSalaries();
        for (Salary s : salaries) {
            if (s.getEmployee().getDepartment().getId() == d.getId())
                result.add(s);
        }
        return result;
    }

    public ObservableList<Salary> getAllSalariesFromDepartmentInOneYear(int year, Department d) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getAllSalariesFromDepartment(d);
        for (Salary s : salaries) {
            if (s.getDate().getYear() == year)
                result.add(s);
        }
        return result;
    }

    public ObservableList<Salary> getAllSalariesFromDepartmentInOneMonth(int month, int year, Department d) {
        ObservableList<Salary> result = FXCollections.observableArrayList(), salaries = getAllSalariesFromDepartmentInOneYear(year, d);
        for (Salary s : salaries) {
            if (s.getDate().getMonthValue() == month)
                result.add(s);
        }
        return result;
    }

    public int getIDOfLastSalaryForEmployee(Employee employee) {
        ObservableList<Salary> salaries = getSalaries();
        int numberOfSalaries = getAllSalariesForEmployee(employee).size();
        for (Salary s : salaries) {
            if (s.getEmployee().getId() == employee.getId()) {
                numberOfSalaries--;
                if (numberOfSalaries == 0)
                    return s.getId();
            }
        }
        return 1;
    }
}
