package ba.unsa.etf.rpr.projekat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
