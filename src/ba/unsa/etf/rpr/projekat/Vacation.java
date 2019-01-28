package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Vacation {
    private int id;
    private LocalDate startOfVacation, endOfVacation;
    private Employee employee;

    public Vacation() {
    }

    public Vacation(int id, LocalDate startOfVacation, LocalDate endOfVacation, Employee employee) {
        this.id = id;
        this.startOfVacation = startOfVacation;
        this.endOfVacation = endOfVacation;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartOfVacation() {
        return startOfVacation;
    }

    public void setStartOfVacation(LocalDate startOfVacation) {
        this.startOfVacation = startOfVacation;
    }

    public LocalDate getEndOfVacation() {
        return endOfVacation;
    }

    public void setEndOfVacation(LocalDate endOfVacation) {
        this.endOfVacation = endOfVacation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
