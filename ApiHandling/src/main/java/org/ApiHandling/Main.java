package org.ApiHandling;
//<--- Json parsing dependencies --->
import org.json.JSONArray;
import org.json.JSONObject;

//<-- Api fetch dependencies --->
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

//<-- Mysql jdbc dependencies -->

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {

    public static String Api_Key = "";
    public static String City;
    public static String apiUrl ="https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid="+Api_Key;
    public static URL url;
    public static HttpURLConnection connection;
    public static BufferedReader in;
    public static StringBuilder response;

    public static String connectionString = "jdbc:mysql://localhost:3306/java_connection?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    public static String username = "";
    public static String password = "";
    public static Connection conn;
    //public static ResultSet rs;
    public static Statement statement;
    public static void main(String[] args) throws IOException,SQLException {
        String City;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a city for weather information:");
        City = input.nextLine();

        String apiUrl ="https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid="+Api_Key;
        try {
            url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseBody = response.toString();

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                String description = weather.getString("description");
                double temperatureKelvin = jsonObject.getJSONObject("main").getDouble("temp");
                int temperatureCelsius = (int)(temperatureKelvin - 273.15);
                String humidity = jsonObject.getJSONObject("main").getInt("humidity")+"%";
                String wind = jsonObject.getJSONObject("wind").getInt("speed")+"%";
                String clouds = jsonObject.getJSONObject("clouds").getInt("all")+"%";
                System.out.println("City: " + City);
                System.out.println("Description: " + description);
                System.out.println("Temperature: " + (temperatureCelsius) + "°C");
                System.out.println("Humidity:"+humidity);
                System.out.println("wind:"+wind);
                System.out.println("Clouds:"+clouds);
                Class.forName("com.mysql.cj.jdbc.Driver");

                try {
                    conn = DriverManager.getConnection(connectionString, username, password);

                    statement = conn.createStatement();
                    String sqlQuery = "INSERT INTO  weather(city,temperature,clouds,humidity,wind) VALUES ('"+City+"','"+(temperatureCelsius)+" C','"+humidity+"','"+wind+"','"+clouds+"')";
                    statement.executeUpdate(sqlQuery);
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }finally {
                    statement.close();
                    conn.close();
                }
            } else {
                System.out.println("Error code " + responseCode);
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}