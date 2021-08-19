import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UserApp {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        try {
            //Creating the connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbName", "root", "root");
            // Access a Statement
            Statement stmt = connection.createStatement();
            int input;
            do {
                System.out.println("*******************************************************");
                System.out.println("    !!!!!!!Welcome to User CRUD Services !!!!!!");
                System.out.println("*******************************************************");
                System.out.println("1. Registration");
                System.out.println("2. Update");
                System.out.println("3. Display User Data");
                System.out.println("4. Delete");
                System.out.println("0. Exit");
                Scanner scan = new Scanner(System.in);
                input = scan.nextInt();
                switch (input) {
                    case 1:
                        System.out.println("Please enter first name");
                        String fName = scan.next();
                        System.out.println("Please enter last name");
                        String lName = scan.next();
                        System.out.println("Please enter email ID");
                        String email = scan.next();
                        System.out.println("Please enter mob number");
                        String mob = scan.next();
                        OkHttpClient client = new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder().url("http://2factor.in/API/V1/8700d13b-fd04-11eb-a13b-0200cd936042/SMS/" + mob + "/AUTOGEN").get()
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .build();
                        Response response = client.newCall(request).execute();
                        System.out.println(response);
                        if (response.code() == 200) {
                            System.out.println("OTP send successfully!");
                            String json = response.body().string();
                            //System.out.println(json);
                            JSONObject Jobject = new JSONObject(json);
                            //System.out.println(Jobject);
                            String sid = (String) Jobject.get("Details");
                            //System.out.println(sid);
                            System.out.println("Please enter OTP sent to " + mob + " to continue");
                            int otp = scan.nextInt();
                            OkHttpClient client1 = new OkHttpClient();
                            okhttp3.Request request1 = new okhttp3.Request.Builder().url("http://2factor.in/API/V1/8700d13b-fd04-11eb-a13b-0200cd936042/SMS/VERIFY/" + sid + "/" + otp).get()
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .build();
                            Response response1 = client1.newCall(request1).execute();
                            if (response1.code() == 200) {
                                System.out.println("OTP Verified successfully!");
                                userDAO.saveData(connection, fName, lName, email);
                                break;
                            } else {
                                System.out.println("Wrong OTP, please try again");
                                break;
                            }
                        } else {
                            System.out.println("Issue while sending OTP, try again!");
                            break;
                        }
                    case 2:
                        System.out.println("Please enter user id to update");
                        int id = scan.nextInt();
                        System.out.println("Please enter new email id to update");
                        String newEmail = scan.next();
                        userDAO.updateUserEmail(connection, id, newEmail);
                        break;
                    case 3:
                        userDAO.getAllUsers(stmt);
                        break;
                    case 4:
                        System.out.println("Please enter user id to delete");
                        int userId = scan.nextInt();
                        userDAO.deleteuser(connection, userId);
                        break;

                    default:
                        break;
                }
            } while (input > 0);
            stmt.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
