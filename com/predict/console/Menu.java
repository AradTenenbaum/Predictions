package com.predict.console;

public class Menu {
    public static void displayMainMenu() {
        int i = 1;
        System.out.println("----------------------");
        System.out.println("Welcome to Predictions");
        System.out.println("----------------------");
        System.out.println("\nWhat would you like to do?");
        System.out.println((i++) + ". Run a simulation");
        System.out.println((i++) + ". Display simulation");
        System.out.println((i++) + ". Load a file (xml)");
        System.out.println((i++) + ". History");
        System.out.println((i++) + ". exit");
    }
}
