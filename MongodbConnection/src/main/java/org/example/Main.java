package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class Main {
    public static String connectionString = "mongodb://127.0.0.1:27017";
    public static MongoClient client;
    public static MongoDatabase database;
    public static void main(String[] args) {

        client = MongoClients.create(connectionString);
        database = client.getDatabase("java_connection");
        System.out.println(database.getName());
    }
}