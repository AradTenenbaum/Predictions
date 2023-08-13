package com.predict.engine.data.source;

import com.predict.engine.data.dto.WorldDto;
import com.predict.engine.data.jaxb.*;
import com.predict.engine.data.validation.Validation;
import com.predict.engine.def.*;
import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.ActionType;
import com.predict.engine.def.action.Calculation;
import com.predict.engine.def.action.condition.Condition;
import com.predict.engine.def.action.condition.MultipleCondition;
import com.predict.engine.def.action.condition.SingleCondition;
import com.predict.engine.simulation.Manager;
import com.predict.engine.utils.exception.FileException;
import com.predict.engine.utils.exception.ValidationException;
import com.predict.engine.utils.object.Range;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class File {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "com.predict.engine.data.jaxb";
    private static Environment environment;
    private static Map<String, Entity> entities;
    private static List<Rule> rules;
    private static Termination termination;
    public static void fetchDataFromFile(String path, Manager manager) throws ValidationException, FileException, Exception {
        try {

            InputStream inputStream = new FileInputStream(new java.io.File(path));
            if(!path.endsWith(".xml")) {
                throw new FileException("File extension is not xml");
            }

            PRDWorld fileWorld = deserializeFrom(inputStream);

            environment = buildEnvironment(fileWorld.getPRDEvironment());
            entities = buildEntities(fileWorld.getPRDEntities());

            rules = buildRules(fileWorld.getPRDRules());

            termination = buildTermination(fileWorld.getPRDTermination());

            World world = new World(environment, entities, rules, termination);
            WorldDto sharedWorld = new WorldDto(world);
            manager.setCurrentWorld(world);
            manager.setSharedWorld(sharedWorld);
            manager.setValidWorld(true);


        } catch (JAXBException e) {
            throw new ValidationException("File might not match scheme");
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new FileException("File not found. try a different path");
        } catch (Exception e) {
            throw new Exception("There is an error. please try again");
        }
    }

    private static Environment buildEnvironment(PRDEvironment fileEnv) throws ValidationException {
        Environment environment = new Environment();
        for(PRDEnvProperty fileEnvProp : fileEnv.getPRDEnvProperty()) {
            Property p;
            try {
                if(environment.getProperties().containsKey(fileEnvProp.getPRDName())) {
                    throw new ValidationException("'" + fileEnvProp.getPRDName() + "' is declared more than once. Property name must be unique");
                }
                p = buildEnvProperty(fileEnvProp);
            } catch (ValidationException e) {
                throw new ValidationException("Environment -> " + e.getMessage());
            }
            environment.addProperty(p);
        }

        return environment;
    }

    private static Map<String, Entity> buildEntities(PRDEntities fileEntities) throws ValidationException {
        Map<String, Entity> entities = new HashMap<>();
        for(PRDEntity fileEntity : fileEntities.getPRDEntity()) {
            Entity e = buildEntity(fileEntity);
            entities.put(e.getName(), e);
        }
        return entities;
    }

    private static Termination buildTermination(PRDTermination fileTermination) {
        PRDByTicks fileTicks = null;
        PRDBySecond fileSeconds = null;
        for(Object fileTerm : fileTermination.getPRDByTicksOrPRDBySecond()) {
            if(fileTerm.getClass().toString().equals("class com.predict.engine.data.jaxb.PRDByTicks"))
                fileTicks = (PRDByTicks)(fileTerm);
            if(fileTerm.getClass().toString().equals("class com.predict.engine.data.jaxb.PRDBySecond"))
                fileSeconds = (PRDBySecond)(fileTerm);
        }
        int ticks = (fileTicks != null ? fileTicks.getCount() : -1);
        int seconds = (fileSeconds != null ? fileSeconds.getCount() : -1);
        return new Termination(ticks, seconds);
    }

    private static List<Rule> buildRules(PRDRules fileRules) throws ValidationException {
        List<Rule> rules = new ArrayList<>();
        for(PRDRule fileRule : fileRules.getPRDRule()) {
            Rule r = buildRule(fileRule);
            rules.add(r);
        }
        return rules;
    }

    private static Rule buildRule(PRDRule fileRule) throws ValidationException {
        String trimmedName = fileRule.getName().trim();

        List<Action> actions;
        try {
            Validation.stringNoSpaceValidation(trimmedName);
            actions = buildActions(fileRule.getPRDActions());
        } catch (ValidationException e) {
            throw new ValidationException("Rule: " + trimmedName + " -> " + e.getMessage());
        }

        int ticks = 1;
        double probability = 1;
        if(fileRule.getPRDActivation() != null) {
            if(fileRule.getPRDActivation().getTicks() != null) ticks = fileRule.getPRDActivation().getTicks();
            if(fileRule.getPRDActivation().getProbability() != null) probability = fileRule.getPRDActivation().getProbability();
        }

        Rule r = new Rule(trimmedName, ticks, probability, actions);
        return r;
    }

    private static List<Action> buildActions(PRDActions fileActions) throws ValidationException {
        List<Action> actions = new ArrayList<>();
        for(PRDAction fileAction : fileActions.getPRDAction()) {
            Action a = buildAction(fileAction);
            actions.add(a);
        }
        return actions;
    }

    private static List<Action> buildActionsList(List<PRDAction> fileActions) throws ValidationException {
        List<Action> actions = new ArrayList<>();
        for(PRDAction fileAction : fileActions) {
            Action a = buildAction(fileAction);
            actions.add(a);
        }
        return actions;
    }

    private static Action buildAction(PRDAction fileAction) throws ValidationException {
        if(fileAction.getType().equals(ActionType.INCREASE) || fileAction.getType().equals(ActionType.DECREASE) ) {
            Validation.isEntityPropertyExists(entities, fileAction.getEntity(), fileAction.getProperty());
            Validation.isPropertyValidNumber(entities, fileAction.getEntity(), fileAction.getProperty());

            if(fileAction.getBy() != null) {
                Validation.isTypeValid(environment, entities, fileAction.getEntity(), fileAction.getProperty(), fileAction.getBy());
            }
            else {
                throw new ValidationException("action increase/decrease have to receive a by value");
            }
            return new Action(fileAction.getType(), fileAction.getEntity(), entities.get(fileAction.getEntity()).getProperties().get(fileAction.getProperty()), fileAction.getBy());
        }
        else if(fileAction.getType().equals(ActionType.CALCULATION)) {
            Validation.isEntityPropertyExists(entities, fileAction.getEntity(), fileAction.getResultProp());

            Calculation.TYPES type;
            String arg1 = "", arg2 = "";
            if(fileAction.getPRDMultiply() != null) {
                type = Calculation.TYPES.MULT;
                arg1 = fileAction.getPRDMultiply().getArg1();
                arg2 = fileAction.getPRDMultiply().getArg2();
            }
            else if(fileAction.getPRDDivide() != null) {
                type = Calculation.TYPES.DIV;
                arg1 = fileAction.getPRDDivide().getArg1();
                arg2 = fileAction.getPRDDivide().getArg2();
            }
            else {
                throw new ValidationException("missing tag in calculation");
            }

            Validation.calculationValidation(environment, entities, fileAction.getEntity(), fileAction.getResultProp(), arg1, arg2);

            return new Calculation(fileAction.getEntity(), entities.get(fileAction.getEntity()).getProperties().get(fileAction.getResultProp()), type, arg1, arg2);
        }
        else if(fileAction.getType().equals(ActionType.CONDITION)) {
            return buildCondition(fileAction.getPRDCondition(), fileAction.getEntity(), fileAction.getProperty(), fileAction.getPRDThen(), fileAction.getPRDElse(), Condition.TYPE.OUTER);
        }
        else if(fileAction.getType().equals(ActionType.SET)) {
            Validation.isEntityPropertyExists(entities, fileAction.getEntity(), fileAction.getProperty());
            Validation.isTypeValid(environment, entities, fileAction.getEntity(), fileAction.getProperty(), fileAction.getValue());
            return new Action(fileAction.getType(), fileAction.getEntity(), entities.get(fileAction.getEntity()).getProperties().get(fileAction.getProperty()), fileAction.getValue());
        }
        else if(fileAction.getType().equals(ActionType.KILL)) {
            Validation.isEntityExists(entities, fileAction.getEntity());
            return new Action(fileAction.getType(), fileAction.getEntity());
        }
        else {
            throw new ValidationException("'" + fileAction.getType() +"' is not a valid action type");
        }
    }

    private static Condition buildCondition(PRDCondition fileCondition, String entity, String property, PRDThen fileThen, PRDElse fileElse, Condition.TYPE t) throws ValidationException {
        List<Action> thenAction = null, elseAction = null;
        if(t == Condition.TYPE.OUTER) {
            thenAction = buildActionsList(fileThen.getPRDAction());
            if(fileElse != null) elseAction = buildActionsList(fileElse.getPRDAction());
        }

        if(fileCondition.getSingularity().equals(Condition.SINGLE)) {
            Validation.isEntityPropertyExists(entities, fileCondition.getEntity(), fileCondition.getProperty());
            if(fileCondition.getValue() != null) {
                Validation.isTypeValid(environment, entities, fileCondition.getEntity(), fileCondition.getProperty(), fileCondition.getValue());
            }
            else {
                throw new ValidationException("condition must contain a value");
            }
            return new SingleCondition(fileCondition.getEntity(), entities.get(fileCondition.getEntity()).getProperties().get(fileCondition.getProperty()), thenAction, elseAction, fileCondition.getOperator(), fileCondition.getValue());
        }
        else if(fileCondition.getSingularity().equals(Condition.MULTIPLE)) {
            List<Condition> conditions = new ArrayList<>();
            for(PRDCondition c1 : fileCondition.getPRDCondition()) {
                Condition c = buildCondition(c1, entity, property, fileThen, fileElse, Condition.TYPE.INNER);
                conditions.add(c);
            }
            return new MultipleCondition(entity, entities.get(entity).getProperties().get(property), thenAction, elseAction, conditions, fileCondition.getLogical());
        }
        return new Condition();
    }

    private static Entity buildEntity(PRDEntity fileEntity) throws ValidationException {
        String trimmedName = fileEntity.getName().trim();
        try {
            Validation.stringNoSpaceValidation(trimmedName);

            if(fileEntity.getPRDPopulation() < 0) {
                throw new ValidationException("Population cannot be negative");
            }

            Entity entity = new Entity(trimmedName, fileEntity.getPRDPopulation());
            for(PRDProperty fileProp : fileEntity.getPRDProperties().getPRDProperty()) {
                Property p;
                if(entity.getProperties().containsKey(fileProp.getPRDName())) {
                    throw new ValidationException("'" + fileProp.getPRDName() + "' is declared more than once. Property name must be unique");
                }
                p = buildProperty(fileProp);
                entity.addProperty(p);
            }

            return entity;
        } catch (ValidationException e) {
            throw new ValidationException("Entity: " + trimmedName + " -> " + e.getMessage());
        }
    }

    private static Property buildPropertyByType(String name, String type, PRDRange range) throws ValidationException {
        String trimmedName = name.trim();
        try {
            Validation.stringNoSpaceValidation(trimmedName);
            if(range != null) Validation.rangeValid(range.getFrom(), range.getTo());
        } catch (ValidationException e) {
            throw new ValidationException("Property: " + name + " -> " + e.getMessage());
        }

        Property p;
        if(type.equals(PropertyType.DECIMAL))
            p = new Property(trimmedName, PropertyType.DECIMAL);
        else if(type.equals(PropertyType.FLOAT))
            p = new Property(trimmedName, PropertyType.FLOAT);
        else if(type.equals(PropertyType.BOOLEAN))
            p = new Property(trimmedName, PropertyType.BOOLEAN);
        else if(type.equals(PropertyType.STRING))
            p = new Property(trimmedName, PropertyType.STRING);
        else
            throw new ValidationException("Property: " + name + " -> " + "type '"+ type +"' is not valid for the system");

        if(range != null) {
            p.setRange(new Range(range.getFrom(), range.getTo()));
        }

        return p;
    }

    private static Property buildEnvProperty(PRDEnvProperty fileEnvProp) throws ValidationException {
        return buildPropertyByType(fileEnvProp.getPRDName(), fileEnvProp.getType(), fileEnvProp.getPRDRange());
    }

    private static Property buildProperty(PRDProperty fileProp) throws ValidationException {
        Property p = buildPropertyByType(fileProp.getPRDName(), fileProp.getType(), fileProp.getPRDRange());

        if(fileProp.getPRDValue().isRandomInitialize()) {
            p.setRandom(true);
        }
        else if(fileProp.getPRDValue().getInit() != null && fileProp.getPRDValue().getInit().length() > 0) {
            try {
                p.setRandom(false);
                Validation.isValueFromType(fileProp.getType(), fileProp.getPRDValue().getInit());
                if((fileProp.getType().equals(PropertyType.DECIMAL) || fileProp.getType().equals(PropertyType.FLOAT)) && fileProp.getPRDRange() != null) Validation.isValidByRange(fileProp.getPRDValue().getInit(), fileProp.getPRDRange().getFrom(), fileProp.getPRDRange().getTo());
            } catch (ValidationException e) {
                throw new ValidationException("Property: " + fileProp.getPRDName() + " -> " + e.getMessage());
            }
            p.setInit(fileProp.getPRDValue().getInit());
        }

        return p;
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
