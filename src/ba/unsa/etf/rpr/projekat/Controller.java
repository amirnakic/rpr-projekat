package ba.unsa.etf.rpr.projekat;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {
    public Tab departmentTab;
    public Tab employeeTab;
    public Tab vacationTab;
    public Tab sickLeaveTab;
    public Tab unpaidLeaveTab;
    public Tab salaryTab;
    public TableView<Department> departmentTable = new TableView<>();
    public TableColumn<Department, Integer> departmentID;
    public TableColumn<Department, String> departmentName;
    public TableColumn<Department, String> availabilityOfDepartment;
    private CompanyDAO company;
    private ObjectProperty<Department> currentDepartment = new SimpleObjectProperty<>();

    public Controller() {
        company = new CompanyDAO();
    }

    public Department getCurrentDepartment() {
        return currentDepartment.get();
    }

    public ObjectProperty<Department> currentDepartmentProperty() {
        return currentDepartment;
    }

    public void setCurrentDepartment(Department currentDepartment) {
        this.currentDepartment.set(currentDepartment);
    }

    @FXML
    public void initialize() {
        departmentID.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
        departmentName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));
        availabilityOfDepartment.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getCurrentNumberOfEmployees(), "/", cellData.getValue().getMaximumNumberOfEmployees()));
        ObservableList<Department> departments = company.getDepartments();
        departmentTable.setItems(departments);
    }

    public void clickOnAddDepartmentButton(ActionEvent actionEvent) {
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/department.fxml"));
            loader.setController(new DepartmentController(company, null));
            Parent root = loader.load();
            myStage.setTitle("Adding a new department");
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
            myStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnChangeDepartmentButton(ActionEvent actionEvent) {
        if (getCurrentDepartment() == null) {
            if (departmentTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentDepartment(departmentTable.getSelectionModel().getSelectedItem());
        }
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/department.fxml"));
            loader.setController(new DepartmentController(company, getCurrentDepartment()));
            Parent root = loader.load();
            myStage.setTitle("Edit of department");
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
            myStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnRemoveDepartmentButton(ActionEvent actionEvent) {
        if (departmentTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentDepartment(departmentTable.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removing department");
        alert.setHeaderText("Are you sure that you want to remove this department ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                company.removeDepartment(getCurrentDepartment());
            } catch (DepartmentException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(e.getMessage());
                alert1.showAndWait();
            }
        }
    }
}
