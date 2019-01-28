package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class UnpaidLeave {
    private int id;
    private LocalDate startOfAbsence, endOfAbsence;
    private Employee employee;

    public UnpaidLeave() {
    }

    public UnpaidLeave(int id, LocalDate startOfAbsence, LocalDate endOfAbsence, Employee employee) {
        this.id = id;
        this.startOfAbsence = startOfAbsence;
        this.endOfAbsence = endOfAbsence;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartOfAbsence() {
        return startOfAbsence;
    }

    public void setStartOfAbsence(LocalDate startOfAbsence) {
        this.startOfAbsence = startOfAbsence;
    }

    public LocalDate getEndOfAbsence() {
        return endOfAbsence;
    }

    public void setEndOfAbsence(LocalDate endOfAbsence) {
        this.endOfAbsence = endOfAbsence;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
