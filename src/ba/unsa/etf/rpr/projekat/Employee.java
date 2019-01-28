package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Employee {
    private int id, workExperience, vacationDaysPerYear;
    private String name, surname, phoneNumber, emailAddress, role, qualifications;
    private LocalDate dateOfBirth, dateOfEmployment;
    private boolean vacation, sickLeave, unpaidLeave;
    private Department department;

    public Employee() {
    }

    public Employee(int id, int workExperience, int vacationDaysPerYear, String name, String surname, String phoneNumber, String emailAddress, String role, String qualifications, LocalDate dateOfBirth, LocalDate dateOfEmployment, boolean vacation, boolean sickLeave, boolean unpaidLeave, Department department) {
        this.id = id;
        this.workExperience = workExperience;
        this.vacationDaysPerYear = vacationDaysPerYear;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.role = role;
        this.qualifications = qualifications;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.vacation = vacation;
        this.sickLeave = sickLeave;
        this.unpaidLeave = unpaidLeave;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public int getVacationDaysPerYear() {
        return vacationDaysPerYear;
    }

    public void setVacationDaysPerYear(int vacationDaysPerYear) {
        this.vacationDaysPerYear = vacationDaysPerYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public boolean isVacation() {
        return vacation;
    }

    public void setVacation(boolean vacation) {
        this.vacation = vacation;
    }

    public boolean isSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(boolean sickLeave) {
        this.sickLeave = sickLeave;
    }

    public boolean isUnpaidLeave() {
        return unpaidLeave;
    }

    public void setUnpaidLeave(boolean unpaidLeave) {
        this.unpaidLeave = unpaidLeave;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return surname + " " + name;
    }
}
