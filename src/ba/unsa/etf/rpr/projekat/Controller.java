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
    public TableView<Employee> employeeTable = new TableView<>();
    public TableColumn<Employee, Integer> employeeID;
    public TableColumn<Employee, String> employeeName;
    public TableColumn<Employee, String> employeeRole;
    public TableColumn<Employee, String> employeeDepartment;
    public TableColumn<Employee, String> employeeAvailability;
    private CompanyDAO company;
    private ObjectProperty<Department> currentDepartment = new SimpleObjectProperty<>();
    private ObjectProperty<Employee> currentEmployee = new SimpleObjectProperty<>();

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

    public Employee getCurrentEmployee() {
        return currentEmployee.get();
    }

    public ObjectProperty<Employee> currentEmployeeProperty() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee.set(currentEmployee);
    }

    @FXML
    public void initialize() {
        departmentID.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
        departmentName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));
        availabilityOfDepartment.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getCurrentNumberOfEmployees(), "/", cellData.getValue().getMaximumNumberOfEmployees()));
        departmentTable.setItems(company.getDepartments());
        employeeID.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));
        employeeName.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getName(), " ", cellData.getValue().getSurname()));
        employeeRole.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));
        employeeDepartment.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getDepartment().getName()));
        employeeAvailability.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().availability()));
        departmentTable.setItems(company.getDepartments());
        employeeTable.setItems(company.getEmployees());
    }

    public void clickOnAddDepartmentButton(ActionEvent actionEvent) {
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/department.fxml"));
            loader.setController(new DepartmentController(company, null, this));
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
            loader.setController(new DepartmentController(company, getCurrentDepartment(), this));
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
                setCurrentDepartment(null);
                departmentTable.setItems(company.getDepartments());
            } catch (DepartmentException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(e.getMessage());
                alert1.showAndWait();
            }
        }
    }

    public void clickOnHireButton(ActionEvent actionEvent) {
    }

    public void clickOnPromoteButton(ActionEvent actionEvent) {
    }

    public void clickOnFireButton(ActionEvent actionEvent) {
    }

    public void clickOnRetireButton(ActionEvent actionEvent) {
    }

    public void clickOnVacationButton(ActionEvent actionEvent) {
    }

    public void clickOnSickLeaveButton(ActionEvent actionEvent) {
    }

    public void clickOnUnpaidLeaveButton(ActionEvent actionEvent) {
    }

    public void clickOnInfoButton(ActionEvent actionEvent) {
    }
}
