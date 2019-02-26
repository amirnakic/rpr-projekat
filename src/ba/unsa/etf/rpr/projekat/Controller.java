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
import org.assertj.core.internal.bytebuddy.asm.Advice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {
    public Tab departmentTab;
    public Tab employeeTab;
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
        updateWorkExperience(company.getEmployees());
        if (LocalDate.now().getMonthValue() == 1 && LocalDate.now().getDayOfMonth() == 1)
            updateSalaries(company.getSalaries());
    }

    public void updateWorkExperience(ObservableList<Employee> employees) {
        for (Employee e : employees) {
            if (DAYS.between(e.getDateOfEmployment(), LocalDate.now()) >= 365 * (1 + e.getWorkExperience()))
                e.setWorkExperience(e.getWorkExperience() + 1);
        }
    }

    public void updateSalaries(ObservableList<Salary> salaries) {
        for (Salary s : salaries) {
            Salary salary = new Salary(s.getId(), s.getBase(), s.getCoefficient(), s.getTaxes(), s.getContributions(), s.getMealAllowances(), LocalDate.now(), s.getEmployee());
            company.addSalary(salary);
        }
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
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee.fxml"));
            loader.setController(new EmployeeController(company, null, this));
            Parent root = loader.load();
            myStage.setTitle("Hiring a new employee");
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
            myStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnPromoteButton(ActionEvent actionEvent) {
        if (getCurrentEmployee() == null) {
            if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        }
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee.fxml"));
            loader.setController(new EmployeeController(company, getCurrentEmployee(), this));
            Parent root = loader.load();
            myStage.setTitle("Promoting an employee");
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
            myStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnFireButton(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Firing an employee");
        alert.setHeaderText("Are you sure that you want to fire this employee ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                company.removeEmployee(getCurrentEmployee());
                setCurrentEmployee(null);
                employeeTable.setItems(company.getEmployees());
                departmentTable.setItems(company.getDepartments());
            } catch (EmployeeException ee) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(ee.getMessage());
                alert1.showAndWait();
            }
        }
    }

    public void clickOnRetireButton(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        if (getCurrentEmployee().getWorkExperience() < 40) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("This employee doesn't meet requirements to be retired.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Retiring an employee");
            alert.setHeaderText("Are you sure that you want to retire this employee ?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    company.removeEmployee(getCurrentEmployee());
                    setCurrentEmployee(null);
                    employeeTable.setItems(company.getEmployees());
                    departmentTable.setItems(company.getDepartments());
                } catch (EmployeeException ee) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setContentText(ee.getMessage());
                    alert1.showAndWait();
                }
            }
        }
    }

    public void clickOnVacationButton(ActionEvent actionEvent) {
        if (getCurrentEmployee() == null) {
            if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sending an employee to vacation");
        alert.setHeaderText("Are you sure that you want to send this employee to vacation ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                Stage myStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/absence.fxml"));
                loader.setController(new AbsenceController(company, getCurrentEmployee(), this, 1));
                Parent root = loader.load();
                myStage.setTitle("Vacation");
                myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                myStage.show();
                myStage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickOnSickLeaveButton(ActionEvent actionEvent) {
        if (getCurrentEmployee() == null) {
            if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sending an employee to sick leave");
        alert.setHeaderText("Are you sure that you want to send this employee to sick leave ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                Stage myStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/absence.fxml"));
                loader.setController(new AbsenceController(company, getCurrentEmployee(), this, 2));
                Parent root = loader.load();
                myStage.setTitle("Sick Leave");
                myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                myStage.show();
                myStage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickOnUnpaidLeaveButton(ActionEvent actionEvent) {
        if (getCurrentEmployee() == null) {
            if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sending an employee to unpaid leave");
        alert.setHeaderText("Are you sure that you want to send this employee to unpaid leave ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                Stage myStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/absence.fxml"));
                loader.setController(new AbsenceController(company, getCurrentEmployee(), this, 3));
                Parent root = loader.load();
                myStage.setTitle("Unpaid Leave");
                myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                myStage.show();
                myStage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickOnInfoButton(ActionEvent actionEvent) {
        if (getCurrentEmployee() == null) {
            if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
            setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        }
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/info.fxml"));
            loader.setController(new InfoController(getCurrentEmployee()));
            Parent root = loader.load();
            myStage.setTitle("Info");
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
            myStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnGetBackFromVacationButton(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Getting an employee back from vacation");
        alert.setHeaderText("Are you sure that you want to get this employee back from vacation ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                company.getEmployeeBackFromVacation(getCurrentEmployee());
                employeeTable.setItems(company.getEmployees());
            } catch (VacationException ee) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(ee.getMessage());
                alert1.showAndWait();
            }
        }
    }

    public void clickOnGetBackFromSickLeaveButton(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Getting an employee back from sick leave");
        alert.setHeaderText("Are you sure that you want to get this employee back from sick leave ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                company.getEmployeeBackFromSickLeave(getCurrentEmployee());
                employeeTable.setItems(company.getEmployees());
            } catch (SickLeaveException ee) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(ee.getMessage());
                alert1.showAndWait();
            }
        }
    }

    public void clcikOnGetBackFromUnpaidLeaveButton(ActionEvent actionEvent) {
        if (employeeTable.getSelectionModel().getSelectedItem() == null) return;
        setCurrentEmployee(employeeTable.getSelectionModel().getSelectedItem());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Getting an employee back from unpaid leave");
        alert.setHeaderText("Are you sure that you want to get this employee back from unpaid leave ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                company.getEmployeeBackFromUnpaidLeave(getCurrentEmployee());
                employeeTable.setItems(company.getEmployees());
            } catch (UnpaidLeaveException ee) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setContentText(ee.getMessage());
                alert1.showAndWait();
            }
        }
    }
}
