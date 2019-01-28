package ba.unsa.etf.rpr.projekat;

public class Salary {
    private int id, base, coefficient, taxes, contributions, mealAllowances;
    private Employee employee;

    public Salary() {
    }

    public Salary(int id, int base, int coefficient, int taxes, int contributions, int mealAllowances, Employee employee) {
        this.id = id;
        this.base = base;
        this.coefficient = coefficient;
        this.taxes = taxes;
        this.contributions = contributions;
        this.mealAllowances = mealAllowances;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getTaxes() {
        return taxes;
    }

    public void setTaxes(int taxes) {
        this.taxes = taxes;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public int getMealAllowances() {
        return mealAllowances;
    }

    public void setMealAllowances(int mealAllowances) {
        this.mealAllowances = mealAllowances;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
