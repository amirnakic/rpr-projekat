package ba.unsa.etf.rpr.projekat;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    }

    public void clickOnAddDepartmentButton(ActionEvent actionEvent) {
    }

    public void clickOnChangeDepartmentButton(ActionEvent actionEvent) {
    }

    public void clickOnRemoveDepartmentButton(ActionEvent actionEvent) {
    }
}
