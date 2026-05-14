package com.errs;

import model.User;
import service.AuthService;

public class TestLogin {

    public static void main(String[] args) {

        AuthService authService =
                new AuthService();

        User user =
                authService.login(

                        "diana@gmail.com",
                        "1234"

                );

        if(user != null) {

            System.out.println(
                    "WELCOME " +
                            user.getFullName()
            );

        } else {

            System.out.println("INVALID LOGIN");
        }
    }
}