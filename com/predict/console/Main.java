package com.predict.console;

import com.predict.engine.data.source.File;
import com.predict.engine.simulation.Manager;
import com.predict.engine.utils.exceptions.FileException;
import com.predict.engine.utils.exceptions.ValidationException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
//        Menu.displayMainMenu();
        Manager manager = new Manager();
        try {
            File.fetchDataFromFile("com/predict/resources/ex1-cigarets.xml", manager);
        } catch (ValidationException | FileException e) {
            System.out.println(e.getMessage());
        }
    }


}
