package ba.unsa.etf.rpr.projekat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InfoController {
    //personal information
    public Label nameLbl;
    public Label surnameLbl;
    public Label phoneNumberLbl;
    public Label emailLbl;
    public Label dateOfBirthLbl;
    //career information
    public Label qLbl;
    public Label experienceLbl;
    public Label dateOfEmploymentLbl;
    //company status
    public Label roleLbl;
    public Label vacationDaysLbl;
    public Label availabilityLbl;
    public Label departmentLbl;

    private Employee employee;

    public InfoController(Employee e) {
        employee = e;
    }

    @FXML
    public void initialize() {
        nameLbl.setText(employee.getName());
        surnameLbl.setText(employee.getSurname());
        phoneNumberLbl.setText(employee.getPhoneNumber());
        emailLbl.setText(employee.getEmailAddress());
        dateOfBirthLbl.setText(employee.getDateOfBirth().toString());
        qLbl.setText(employee.getQualifications());
        experienceLbl.setText(String.valueOf(employee.getWorkExperience()));
        dateOfEmploymentLbl.setText(employee.getDateOfEmployment().toString());
        roleLbl.setText(employee.getRole());
        vacationDaysLbl.setText(String.valueOf(employee.getVacationDaysPerYear()));
        availabilityLbl.setText(employee.availability());
        departmentLbl.setText(employee.getDepartment().getName());
    }
}
