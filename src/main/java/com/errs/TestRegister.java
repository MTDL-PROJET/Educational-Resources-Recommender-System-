package com.errs;

import service.AuthService;

public class TestRegister {

    public static void main(String[] args) {

        AuthService authService =
                new AuthService();

        boolean success =
                authService.register(

                        "Diana Rotaru",
                        "diana@gmail.com",
                        "1234"

                );

        if(success) {

            System.out.println("REGISTER SUCCESS");

        } else {

            System.out.println("REGISTER FAILED");
        }
    }
}