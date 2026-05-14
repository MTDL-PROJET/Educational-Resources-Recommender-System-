package com.errs;

import service.UserService;

public class TestExpertRequest {

    public static void main(String[] args) {

        UserService userService =
                new UserService();

        boolean success =
                userService.requestExpertRole(1);

        if(success) {

            System.out.println("REQUEST SENT");

        } else {

            System.out.println("FAILED");
        }
    }
}