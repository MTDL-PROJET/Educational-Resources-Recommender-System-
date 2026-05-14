package com.errs;

import model.User;
import service.ResourceService;

public class TestResource {

    public static void main(String[] args) {

        ResourceService resourceService =
                new ResourceService();

        boolean success =
                resourceService.addResource(

                        "Java Backend Course",

                        "Learn backend development with Java.",

                        "https://image.com/java.png",

                        "https://youtube.com",

                        "Programming",

                        1

                );

        if(success) {

            System.out.println("RESOURCE ADDED");

        } else {

            System.out.println("FAILED");
        }
    }
}