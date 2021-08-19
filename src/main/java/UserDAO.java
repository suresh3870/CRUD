import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    public void getAllUsers(Statement stmt) throws SQLException {
        String str = "select idusers,firstName,lastName,email from users";
        ResultSet rs = stmt.executeQuery(str);
        int rowCounter = 0;
        System.out.println("******************User details****************");
        System.out.println("ID         firstName         lastName         email");
        while (rs.next()) {
            int idusers = rs.getInt("idusers");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String email = rs.getString("email");
            System.out.println(idusers+ "           "+firstName+"            "+lastName+"             "+email);
            ++rowCounter;
        }
        System.out.println("Count of records: " + rowCounter);
    }

    public void saveData(Connection con, String fName, String lName, String email) throws SQLException {
        if (con != null && !con.isClosed()) {
            String str = "insert into users (firstName,lastName,email) values (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(str);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, email);
            int row = preparedStatement.executeUpdate();
            System.out.println(row+" row has been inserted successfully");
        }

    }

    public void deleteuser(Connection con, int id) throws SQLException {
        String str = "delete from users where idusers =?";
        PreparedStatement preparedStatement = con.prepareStatement(str);
        preparedStatement.setInt(1, id);
        int row = preparedStatement.executeUpdate();
        System.out.println("The delete recorded:- " + row);
    }

    public void updateUserEmail(Connection con, int id, String email) throws SQLException {
        String str = "update users set email = ? where idusers =?";
        PreparedStatement preparedStatement = con.prepareStatement(str);
        preparedStatement.setString(1, email);
        preparedStatement.setInt(2, id);
        int row = preparedStatement.executeUpdate();
        System.out.println(row+" Record updated successfully");
    }
}