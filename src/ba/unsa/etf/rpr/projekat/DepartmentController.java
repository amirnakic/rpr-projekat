package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DepartmentController {
    public TextField departmentName;
    public TextField maximumNumberOfEmployees;
    private CompanyDAO company;
    private Department department;
    private Controller controller;
    private boolean edit, isNameCorrect, isNumberCorrect;

    public DepartmentController(CompanyDAO c, Department d, Controller co) {
        company = c;
        department = d;
        controller = co;
        if (d == null) edit = false;
        else edit = true;
    }

    @FXML
    public void initialize() {
        if (edit) {
            departmentName.setText(department.getName());
            maximumNumberOfEmployees.setText(String.valueOf(department.getMaximumNumberOfEmployees()));
        }
    }

    public boolean isNumberCorrect(String s) {
        if (s.isEmpty()) return false;
        char[] charSequence = s.toCharArray();
        for (int i = 0; i < charSequence.length; i++) {
            if (!Character.isDigit(charSequence[i]))
                return false;
        }
        if (Integer.parseInt(s) == 0) return false;
        return true;
    }

    public void clickOnOkButton(ActionEvent actionEvent) {
        if (!departmentName.getText().isEmpty()) {
            departmentName.getStyleClass().removeAll("fieldIncorrect");
            departmentName.getStyleClass().add("fieldCorrect");
            isNameCorrect = true;
        } else {
            departmentName.getStyleClass().removeAll("fieldCorrect");
            departmentName.getStyleClass().add("fieldIncorrect");
            isNameCorrect = false;
        }
        if (isNumberCorrect(maximumNumberOfEmployees.getText())) {
            maximumNumberOfEmployees.getStyleClass().removeAll("fieldIncorrect");
            maximumNumberOfEmployees.getStyleClass().add("fieldCorrect");
            isNumberCorrect = true;
        } else {
            maximumNumberOfEmployees.getStyleClass().removeAll("fieldCorrect");
            maximumNumberOfEmployees.getStyleClass().add("fieldIncorrect");
            isNumberCorrect = false;
        }
        Department d = null;
        if (isNameCorrect && isNumberCorrect) {
            if (!edit) {
                d = new Department(company.availableIDForDepartments(company.getDepartments()), 0, Integer.parseInt(maximumNumberOfEmployees.getText()), departmentName.getText());
                try {
                    company.addDepartment(d);
                    controller.departmentTable.setItems(company.getDepartments());
                } catch (DepartmentException e) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(e.getMessage());
                    alert1.showAndWait();
                }
            } else {
                d = new Department(department.getId(), department.getCurrentNumberOfEmployees(), Integer.parseInt(maximumNumberOfEmployees.getText()), departmentName.getText());
                company.changeDepartment(d);
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
