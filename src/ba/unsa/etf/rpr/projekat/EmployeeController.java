package ba.unsa.etf.rpr.projekat;

public class EmployeeController {
    private CompanyDAO company;
    private Employee employee;
    private Controller controller;
    private boolean edit;

    public EmployeeController(CompanyDAO c, Employee e, Controller co) {
        this.company = c;
        this.employee = e;
        if (e == null) edit = false;
        else edit = true;
        this.controller = co;
    }
}
