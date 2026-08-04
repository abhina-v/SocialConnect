package com.interestconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.TimeZone;

public class JdbcTest {

    public static void main(String[] args) {

        // Force JVM timezone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.out.println("------------------------------------------------");
        System.out.println("TimeZone.getDefault() : " + TimeZone.getDefault().getID());
        System.out.println("ZoneId.systemDefault(): " + ZoneId.systemDefault());
        System.out.println("user.timezone         : " + System.getProperty("user.timezone"));
        System.out.println("------------------------------------------------");

        String url = "jdbc:postgresql://localhost:5432/interest_connect";
        String username = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            System.out.println("✅ Connected Successfully!");

        } catch (SQLException e) {

            System.out.println("❌ Connection Failed");
            e.printStackTrace();

        }
    }
}