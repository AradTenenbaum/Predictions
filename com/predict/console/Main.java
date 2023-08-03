package com.predict.console;

import com.predict.data.jaxb.PRDWorld;
import com.predict.data.source.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
//        Menu.displayMainMenu();
        File.fetchDataFromFile();
    }


}
