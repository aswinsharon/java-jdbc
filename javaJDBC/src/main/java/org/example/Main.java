package org.example;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class Main {
    public static String connectionString = "jdbc:mysql://localhost:3306/java_connection?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    public static String username = "root";
    public static String password = "";
    public static Connection conn;
    public static ResultSet rs;
    public static Statement statement;
    public static String temperature,city;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            conn = DriverManager.getConnection(connectionString, username, password);

            statement = conn.createStatement();
            String sqlQuery = "SELECT * FROM weather";
            rs = statement.executeQuery(sqlQuery);
            while (rs.next()){
                city = rs.getString("city");
                temperature = rs.getString("temperature");
            }
            System.out.println("The temperature of city "+city+" is "+temperature);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            statement.close();
            conn.close();
        }
    }
}