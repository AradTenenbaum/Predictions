package com.predict.console;

import com.predict.engine.simulation.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Menu menu = new Menu(manager);
        Boolean isOn = true;
        while (isOn) {
            menu.displayMainMenu();
            menu.choose();
        }
    }


}
