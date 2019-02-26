package ba.unsa.etf.rpr.projekat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Boolean.FALSE;
import static java.time.temporal.ChronoUnit.DAYS;

public class EmployeeController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public TextField nameField;
    public TextField surnameField;
    public TextField phoneNumberField;
    public TextField emailAddressField;
    public TextField workExperienceField;
    public TextField roleField;
    public TextField vacationDaysField;
    public ComboBox<String> qualificationsCombo = new ComboBox<>();
    public ComboBox<Department> departmentCombo = new ComboBox<>();
    public DatePicker dateField;
    private CompanyDAO company;
    private Employee employee;
    private Controller controller;
    private boolean edit, areNamesCorrect, isPhoneNumberCorrect, isEmailCorrect, areNumbersCorrect, isDateCorrect, areCombosCorrect;

    public EmployeeController(CompanyDAO c, Employee e, Controller co) {
        this.company = c;
        this.employee = e;
        if (e == null) edit = false;
        else edit = true;
        this.controller = co;
    }

    @FXML
    public void initialize() {
        dateField.setConverter(new StringConverter<LocalDate>() {
            String pattern = "d.M.yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dateField.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        updateWorkExperience(company.getEmployees());
        qualificationsCombo.getItems().addAll("Elementary School", "High School", "Bachelor degree", "Masters degree", "Doctorate", "Docent");
        departmentCombo.getItems().addAll(company.getDepartments());
        if (edit) {
            nameField.setText(employee.getName());
            surnameField.setText(employee.getSurname());
            phoneNumberField.setText(employee.getPhoneNumber());
            emailAddressField.setText(employee.getEmailAddress());
            dateField.setValue(employee.getDateOfBirth());
            workExperienceField.setText(String.valueOf(employee.getWorkExperience()));
            roleField.setText(employee.getRole());
            vacationDaysField.setText(String.valueOf(employee.getVacationDaysPerYear()));
            qualificationsCombo.setValue(employee.getQualifications());
            departmentCombo.setValue(employee.getDepartment());
        }
    }

    public void updateWorkExperience(ObservableList<Employee> employees) {
        for (Employee e : employees) {
            if (DAYS.between(e.getDateOfEmployment(), LocalDate.now()) >= 365 * (1 + e.getWorkExperience()))
                e.setWorkExperience(e.getWorkExperience() + 1);
        }
    }

    public boolean dateValidation(String s) {
        if (s.isEmpty()) return false;
        String[] data = s.split("-");
        if (data.length != 3) return false;
        String year = data[0], month = data[1], day = data[2];
        for (int i = 0; i < data[0].length(); i++) {
            if (!Character.isDigit(data[0].charAt(i)))
                return false;
        }
        for (int i = 0; i < data[1].length(); i++) {
            if (!Character.isDigit(data[1].charAt(i)))
                return false;
        }
        for (int i = 0; i < data[2].length(); i++) {
            if (!Character.isDigit(data[2].charAt(i)))
                return false;
        }
        if (LocalDate.now().compareTo(dateField.getValue()) < 0) return false;
        return true;
    }

    public boolean isNumberCorrect(String s) {
        if (s.isEmpty()) return false;
        char[] charSequence = s.toCharArray();
        for (int i = 0; i < charSequence.length; i++) {
            if (!Character.isDigit(charSequence[i]))
                return false;
        }
        return true;
    }

    public static boolean emailValidation(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean phoneNumberValidation(String nmbr) {
        if (nmbr.isEmpty()) return false;
        Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
        Pattern pattern1 = Pattern.compile("\\d{3}-\\d{6}");
        Matcher matcher = pattern.matcher(nmbr);
        Matcher matcher1 = pattern1.matcher(nmbr);
        return (matcher.matches() || matcher1.matches());
    }

    public void clickOnOkButton(ActionEvent actionEvent) {
        if (!nameField.getText().isEmpty() && !surnameField.getText().isEmpty() && !roleField.getText().isEmpty()) {
            nameField.getStyleClass().removeAll("fieldIncorrect");
            nameField.getStyleClass().add("fieldCorrect");
            surnameField.getStyleClass().removeAll("fieldIncorrect");
            surnameField.getStyleClass().add("fieldCorrect");
            roleField.getStyleClass().removeAll("fieldIncorrect");
            roleField.getStyleClass().add("fieldCorrect");
            areNamesCorrect = true;
        } else {
            nameField.getStyleClass().removeAll("fieldCorrect");
            nameField.getStyleClass().add("fieldIncorrect");
            surnameField.getStyleClass().removeAll("fieldCorrect");
            surnameField.getStyleClass().add("fieldIncorrect");
            roleField.getStyleClass().removeAll("fieldCorrect");
            roleField.getStyleClass().add("fieldIncorrect");
            areNamesCorrect = false;
        }
        if (isNumberCorrect(workExperienceField.getText()) && isNumberCorrect(vacationDaysField.getText())) {
            workExperienceField.getStyleClass().removeAll("fieldIncorrect");
            workExperienceField.getStyleClass().add("fieldCorrect");
            vacationDaysField.getStyleClass().removeAll("fieldIncorrect");
            vacationDaysField.getStyleClass().add("fieldCorrect");
            areNumbersCorrect = true;
        } else {
            workExperienceField.getStyleClass().removeAll("fieldCorrect");
            workExperienceField.getStyleClass().add("fieldIncorrect");
            vacationDaysField.getStyleClass().removeAll("fieldCorrect");
            vacationDaysField.getStyleClass().add("fieldIncorrect");
            areNumbersCorrect = false;
        }
        if (dateField.getValue() != null && dateValidation(dateField.getValue().toString())) {
            dateField.getStyleClass().removeAll("fieldIncorrect");
            dateField.getStyleClass().add("fieldCorrect");
            isDateCorrect = true;
        } else {
            dateField.getStyleClass().removeAll("fieldCorrect");
            dateField.getStyleClass().add("fieldIncorrect");
            isDateCorrect = false;
        }
        if (departmentCombo.getValue() == null || qualificationsCombo.getValue() == null) {
            areCombosCorrect = false;
        } else {
            areCombosCorrect = true;
        }
        if (phoneNumberValidation(phoneNumberField.getText())) {
            phoneNumberField.getStyleClass().removeAll("fieldIncorrect");
            phoneNumberField.getStyleClass().add("fieldCorrect");
            isPhoneNumberCorrect = true;
        } else {
            phoneNumberField.getStyleClass().removeAll("fieldCorrect");
            phoneNumberField.getStyleClass().add("fieldIncorrect");
            isPhoneNumberCorrect = false;
        }
        if (emailValidation(emailAddressField.getText())) {
            emailAddressField.getStyleClass().removeAll("fieldIncorrect");
            emailAddressField.getStyleClass().add("fieldCorrect");
            isEmailCorrect = true;
        } else {
            emailAddressField.getStyleClass().removeAll("fieldCorrect");
            emailAddressField.getStyleClass().add("fieldIncorrect");
            isEmailCorrect = false;
        }
        Employee e = null;
        if (areNamesCorrect && isPhoneNumberCorrect && isEmailCorrect && areNumbersCorrect && isDateCorrect && areCombosCorrect) {
            if (!edit) {
                e = new Employee(company.availableIDForEmployees(company.getEmployees()), Integer.parseInt(workExperienceField.getText()), Integer.parseInt(vacationDaysField.getText()),
                        nameField.getText(), surnameField.getText(), phoneNumberField.getText(), emailAddressField.getText(), roleField.getText(),
                        qualificationsCombo.getValue(), dateField.getValue(), LocalDate.now(), FALSE, FALSE, FALSE, departmentCombo.getValue());
                try {
                    company.addEmployee(e);
                    controller.employeeTable.setItems(company.getEmployees());
                    controller.departmentTable.setItems(company.getDepartments());
                } catch (EmployeeException e1) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(e1.getMessage());
                    alert1.showAndWait();
                }
            } else {
                e = new Employee(employee.getId(), Integer.parseInt(workExperienceField.getText()), Integer.parseInt(vacationDaysField.getText()),
                        nameField.getText(), surnameField.getText(), phoneNumberField.getText(), emailAddressField.getText(), roleField.getText(),
                        qualificationsCombo.getValue(), dateField.getValue(), employee.getDateOfEmployment(), employee.isVacation(), employee.isSickLeave(),
                        employee.isUnpaidLeave(), departmentCombo.getValue());
                company.changeEmployee(employee);
                controller.employeeTable.setItems(company.getEmployees());
                controller.departmentTable.setItems(company.getDepartments());
            }
            clickOnCancelButton(actionEvent);
        }
    }

    public void clickOnCancelButton(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
