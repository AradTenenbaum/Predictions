package com.predict.data.source;

import com.predict.data.jaxb.PRDEntities;
import com.predict.data.jaxb.PRDEnvProperty;
import com.predict.data.jaxb.PRDEvironment;
import com.predict.data.jaxb.PRDWorld;
import com.predict.engine.def.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class File {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "com.predict.data.jaxb";

    public static World fetchDataFromFile() {
        try {
            InputStream inputStream = new FileInputStream(new java.io.File("com/predict/resources/ex1-cigarets.xml"));
            PRDWorld world = deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return new World();
    }

    private static Environment buildEnvironment(PRDEvironment fileEnv) {
        Environment environment = new Environment();
        for(PRDEnvProperty envProp : fileEnv.getPRDEnvProperty()) {
            Property p = buildPropertyByType(envProp);
            System.out.println(p.toString());
            environment.addProperty(p);
        }

        return environment;
    }

    private static Map<String, Entity> buildEntities(PRDEntities fileEntities) {
        Map<String, Entity> entities = new HashMap<>();

        return entities;
    }

    private static Property buildPropertyByType(PRDEnvProperty fileEnvProp) {
        Property p;
        if(fileEnvProp.getType().equals(PropertyType.DECIMAL))
            p = new Property<Integer>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.FLOAT))
            p = new Property<Float>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.BOOLEAN))
            p = new Property<Boolean>(fileEnvProp.getPRDName());
        if(fileEnvProp.getType().equals(PropertyType.STRING))
            p = new Property<String>(fileEnvProp.getPRDName());
        else
            p = new Property<Integer>(fileEnvProp.getPRDName());

        if(fileEnvProp.getPRDRange() != null) {
            p.setRange(new Range(fileEnvProp.getPRDRange().getFrom(), fileEnvProp.getPRDRange().getTo()));
        }

        return p;
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
