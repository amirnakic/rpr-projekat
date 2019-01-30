package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Comparator;

public class CompanyDAO {
    private Connection conn;
    private PreparedStatement statement;

    public CompanyDAO() {
    }

    public void prepareStatement(String s) {
        try {
            statement = conn.prepareStatement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start(String s) {
        try {
            String url = "jdbc:sqllite:database.db";
            conn = DriverManager.getConnection(url);
            prepareStatement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Department> getDepartments() {
        ObservableList<Department> result = FXCollections.observableArrayList();
        try {
            start("SELECT * FORM department");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Department d = new Department();
                d.setId(rs.getInt(1));
                d.setCurrentNumberOfEmployees(rs.getInt(3));
                d.setMaximumNumberOfEmployees(rs.getInt(4));
                d.setName(rs.getString(2));
                result.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
            return result;
        }
        Comparator<Department> comparator = Comparator.comparingInt(Department::getId);
        result.sort(comparator);
        close();
        return result;
    }
}
