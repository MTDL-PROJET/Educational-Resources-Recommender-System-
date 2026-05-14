package com.errs;

import config.DatabaseConfig;

public class TestConnection {

    public static void main(String[] args) {

        if(DatabaseConfig.getConnection() != null) {

            System.out.println("CONNECTED");

        } else {

            System.out.println("FAILED");
        }
    }
}