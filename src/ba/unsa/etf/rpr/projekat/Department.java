package ba.unsa.etf.rpr.projekat;

public class Department {
    private int id, currentNumberOfEmployees, maximumNumberOfEmployees;
    private String name;

    public Department() {
    }

    public Department(int id, int currentNumberOfEmployees, int maximumNumberOfEmployees, String name) {
        this.id = id;
        this.currentNumberOfEmployees = currentNumberOfEmployees;
        this.maximumNumberOfEmployees = maximumNumberOfEmployees;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentNumberOfEmployees() {
        return currentNumberOfEmployees;
    }

    public void setCurrentNumberOfEmployees(int currentNumberOfEmployees) {
        this.currentNumberOfEmployees = currentNumberOfEmployees;
    }

    public int getMaximumNumberOfEmployees() {
        return maximumNumberOfEmployees;
    }

    public void setMaximumNumberOfEmployees(int maximumNumberOfEmployees) {
        this.maximumNumberOfEmployees = maximumNumberOfEmployees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
