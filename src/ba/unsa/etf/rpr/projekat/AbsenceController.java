package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AbsenceController {
    public DatePicker startOfAbsence;
    public DatePicker endOfAbsence;
    private CompanyDAO company;
    private Employee employee;
    private Controller controller;
    private int typeOfAbsence; // 1 - vacation, 2 - sick leave, 3 - unpaid leave
    private boolean isDateCorrect1, isDateCorrect2;

    public AbsenceController(CompanyDAO c, Employee e, Controller co, int x) {
        company = c;
        employee = e;
        controller = co;
        typeOfAbsence = x;
    }

    @FXML
    public void initialize() {
        startOfAbsence.setConverter(new StringConverter<LocalDate>() {
            String pattern = "d.M.yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                startOfAbsence.setPromptText(pattern.toLowerCase());
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
        endOfAbsence.setConverter(new StringConverter<LocalDate>() {
            String pattern = "d.M.yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                endOfAbsence.setPromptText(pattern.toLowerCase());
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
        if (LocalDate.now().compareTo(startOfAbsence.getValue()) < 0) return false;
        if (endOfAbsence.getValue().compareTo(startOfAbsence.getValue()) < 0) return false;
        return true;
    }

    public void clickOnOkButton(ActionEvent actionEvent) {
        if (startOfAbsence.getValue() != null && dateValidation(startOfAbsence.getValue().toString())) {
            startOfAbsence.getStyleClass().removeAll("fieldIncorrect");
            startOfAbsence.getStyleClass().add("fieldCorrect");
            isDateCorrect1 = true;
        } else {
            startOfAbsence.getStyleClass().removeAll("fieldCorrect");
            startOfAbsence.getStyleClass().add("fieldIncorrect");
            isDateCorrect1 = false;
        }
        if (endOfAbsence.getValue() != null && dateValidation(endOfAbsence.getValue().toString())) {
            endOfAbsence.getStyleClass().removeAll("fieldIncorrect");
            endOfAbsence.getStyleClass().add("fieldCorrect");
            isDateCorrect2 = true;
        } else {
            endOfAbsence.getStyleClass().removeAll("fieldCorrect");
            endOfAbsence.getStyleClass().add("fieldIncorrect");
            isDateCorrect2 = false;
        }

        if (isDateCorrect1 && isDateCorrect2) {
            if (typeOfAbsence == 1) {
                Vacation v = new Vacation(company.availableIDForVacations(company.getVacations()), startOfAbsence.getValue(), endOfAbsence.getValue(), employee);
                try {
                    company.sendEmployeeOnVacation(v);
                    controller.employeeTable.setItems(company.getEmployees());
                } catch (VacationException e) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(e.getMessage());
                    alert1.showAndWait();
                }
            }
            if (typeOfAbsence == 2) {
                SickLeave sl = new SickLeave(company.availableIDForSickLeaves(company.getSickLeaves()), startOfAbsence.getValue(), endOfAbsence.getValue(), employee);
                try {
                    company.sendEmployeeOnSickLeave(sl);
                    controller.employeeTable.setItems(company.getEmployees());
                } catch (SickLeaveException sle) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(sle.getMessage());
                    alert1.showAndWait();
                }
            }
            if (typeOfAbsence == 3) {
                UnpaidLeave ul = new UnpaidLeave(company.availableIDForUnpaidLeaves(company.getUnpaidLeaves()), startOfAbsence.getValue(), endOfAbsence.getValue(), employee);
                try {
                    company.sendEmployeeOnUnpaidLeave(ul);
                    company.removeSalaryBecauseOfUnpaidLeave(ul.getEmployee());
                    controller.employeeTable.setItems(company.getEmployees());
                } catch (UnpaidLeaveException ule) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(ule.getMessage());
                    alert1.showAndWait();
                }
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
