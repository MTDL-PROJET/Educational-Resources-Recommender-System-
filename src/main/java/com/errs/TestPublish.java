package com.errs;

import service.ResourceService;

public class TestPublish {

    public static void main(String[] args) {

        ResourceService resourceService =
                new ResourceService();

        boolean success =
                resourceService.publishResource(1);

        if(success) {

            System.out.println("RESOURCE PUBLISHED");

        } else {

            System.out.println("FAILED");
        }
    }
}